package nl.um.cds.triplifierboot.service;

import nl.um.cds.triplifier.DatabaseInspector;
import nl.um.cds.triplifier.ForeignKeySpecification;
import nl.um.cds.triplifier.rdf.AnnotationFactory;
import nl.um.cds.triplifier.rdf.DataFactory;
import nl.um.cds.triplifier.rdf.OntologyFactory;
import nl.um.cds.triplifierboot.config.TaskProperties;
import nl.um.cds.triplifierboot.entity.TaskEntity;
import nl.um.cds.triplifierboot.repository.TaskRepository;
import nl.um.cds.triplifierboot.rest.controller.dto.TaskDto;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import org.eclipse.rdf4j.model.Statement;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@EnableConfigurationProperties(TaskProperties.class)
public class TaskService {

    private Logger logger = LoggerFactory.getLogger(TaskService.class);

    private TaskRepository taskRepository;

    private TaskProperties taskProperties;

    public TaskService(TaskRepository taskRepository, TaskProperties taskProperties) {
        this.taskRepository = taskRepository;
        this.taskProperties = taskProperties;
    }


    @Scheduled(fixedRateString ="${scheduling.poll-ms}", initialDelay=1000)
    public void taskPoll() {
//        logger.info("task poll");
    }

    private String getTaskPath(String identifier){
        return taskProperties.getWorkdir() + "/" + identifier;
    }

    private String getTaskFilePath(String identifier, String filePath){
        return taskProperties.getWorkdir() + "/" + identifier + "/" + filePath;
    }

    public TaskDto createTask(MultipartFile file) throws IOException {
        String identifier = FilenameUtils.removeExtension(file.getOriginalFilename());

        Path workDirTask = Paths.get(getTaskPath(identifier));
        Files.createDirectories(workDirTask);
        logger.info("Clean workdir {}", workDirTask);
        cleanupDirectory(workDirTask);

        FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(getTaskFilePath(identifier, file.getOriginalFilename())));

        TaskDto dto = new TaskDto();
        dto.setIdentifier(identifier); //maybe generate id?
        runTask(identifier);
        return dto;
    }

    @Async
    public void runTask(String identifier){

        String propertiesFilePath = taskProperties.getPropertiesFile();

        String ontologyFilePath = getOntologyFile(identifier).getAbsolutePath();
        String outputFilePath = getOutputFile(identifier).getAbsolutePath();

        Properties props = new Properties();

        String baseUri = null;

        boolean clearDataGraph = taskProperties.isClearDataGraph();
        boolean ontologyParsing = taskProperties.isOntologyParsing();
        boolean dataParsing = taskProperties.isDataParsing();


        try {
            logger.debug(new File(propertiesFilePath).getAbsolutePath());
            FileInputStream fis = new FileInputStream(new File(propertiesFilePath));
            props.load(fis);
        } catch (IOException e) {
            logger.error("Could not find properties file (" + propertiesFilePath + ", or specified using the -p argument).");
        }

        OntologyFactory of = new OntologyFactory(props);
        if(baseUri != null) {
            of = new OntologyFactory(baseUri, props);
        }
        DataFactory df = new DataFactory(of, props);
        AnnotationFactory af = new AnnotationFactory(props);

        if (clearDataGraph) {
            List<Statement> ontologyStatements = of.getAllStatementsInContext();
            List<Statement> annotationStatements = af.getAllStatementsInContext();
            df.clearData(true);
            if(!ontologyParsing) {
                of.addStatements(ontologyStatements);
            }
            af.addStatements(annotationStatements);
        }

        try {
            if(ontologyParsing) {
                logger.info("Start extracting ontology: " + System.currentTimeMillis());
                DatabaseInspector dbInspect = new DatabaseInspector(props);
                createOntology(dbInspect, of, ontologyFilePath);
                logger.info("Done extracting ontology: " + System.currentTimeMillis());
                logger.info("Ontology exported to " + ontologyFilePath);
            }

            if(dataParsing) {
                logger.info("Start extracting data: " + System.currentTimeMillis());
                df.convertData();
                if ("memory".equals(props.getProperty("repo.type", "memory"))) {
                    logger.info("Start exporting data file: " + System.currentTimeMillis());
                    df.exportData(outputFilePath);
                    logger.info("Data exported to " + outputFilePath);
                }
                logger.info("Done: " + System.currentTimeMillis());

            }
        } catch (SQLException e) {
            logger.error("Could not connect to database with url " + props.getProperty("jdbc.url"));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOntology(DatabaseInspector dbInspect, OntologyFactory of, String ontologyOutputFilePath) throws SQLException {
        for(Map<String,String> tableName : dbInspect.getTableNames()) {
            logger.info("Table name: " + tableName);
            List<String> columns = dbInspect.getColumnNames(tableName.get("name"));
            List<String> primaryKeys = dbInspect.getPrimaryKeyColumns(tableName.get("catalog"), tableName.get("schema"), tableName.get("name"));
            List<ForeignKeySpecification> foreignKeys = dbInspect.getForeignKeyColumns(tableName.get("catalog"), tableName.get("schema"), tableName.get("name"));

            of.processTable(tableName.get("name"), columns, primaryKeys, foreignKeys, tableName.get("schema"), tableName.get("catalog"));
        }

        try {
            of.exportData(ontologyOutputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public TaskDto save(TaskDto task) {
        return task;
    }

    public File getOntologyFile(String taskId) {
        String path = getTaskFilePath(taskId, taskProperties.getOntologyFile());
        return new File(path);
    }

    public File getOutputFile(String taskId) {
        String path = getTaskFilePath(taskId, taskProperties.getOutputFile());
        return new File(path);
    }

    public static void cleanupDirectory(Path dir) throws IOException {
        Files.walk(dir)
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(File::delete);
    }
}
