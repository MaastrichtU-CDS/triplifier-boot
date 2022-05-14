package nl.um.cds.triplifierboot.service;

import nl.um.cds.triplifier.DatabaseInspector;
import nl.um.cds.triplifier.ForeignKeySpecification;
import nl.um.cds.triplifier.rdf.AnnotationFactory;
import nl.um.cds.triplifier.rdf.DataFactory;
import nl.um.cds.triplifier.rdf.OntologyFactory;
import nl.um.cds.triplifierboot.config.TaskProperties;
import nl.um.cds.triplifierboot.entity.TaskEntity;
import nl.um.cds.triplifierboot.repository.TaskRepository;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.rdf4j.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@EnableConfigurationProperties(TaskProperties.class)
public class SchedulingService {

    private Logger logger = LoggerFactory.getLogger(SchedulingService.class);

    private TaskRepository taskRepository;

    private TaskProperties taskProperties;
    private TaskService taskService;

    public SchedulingService(TaskRepository taskRepository, TaskProperties taskProperties, TaskService taskService) {
        this.taskRepository = taskRepository;
        this.taskProperties = taskProperties;
        this.taskService = taskService;
    }


    @Scheduled(fixedRateString ="${scheduling.poll-ms:1000}", initialDelay=1000)
    public void taskPoll() {
        TaskEntity runningTask = taskRepository.getTopByStatusOrderByDateCreatedAsc(TaskEntity.Status.RUNNING);
        if(runningTask!=null){
            logger.info("Task running={}", runningTask.getId());
            return;
        }

        TaskEntity nextTask = taskRepository.getTopByStatusOrderByDateCreatedAsc(TaskEntity.Status.QUEUE);
        if(nextTask!=null) {
            logger.info("Task new task={}", nextTask.getId());
            taskService.runTask(nextTask);
        }
    }

}
