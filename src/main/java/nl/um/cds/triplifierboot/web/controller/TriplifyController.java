package nl.um.cds.triplifierboot.web.controller;

import nl.um.cds.triplifierboot.entity.TaskEntity;
import nl.um.cds.triplifierboot.service.TaskService;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TriplifyController {

    private static final Logger logger = LoggerFactory.getLogger(TriplifyController.class);

    private TaskService taskService;

    public TriplifyController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/task")
    public TaskEntity newTask(@RequestBody TaskEntity task) {
        return taskService.save(task);
    }

    @PostMapping("/binary")
    public ResponseEntity<TaskEntity> createTask(@RequestParam("file") MultipartFile file) throws IOException {
        TaskEntity task = taskService.createTask(file);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/task/{identifier}/output-file")
    public ResponseEntity<Void> getOutputFile(@PathVariable String identifier, HttpServletResponse response) throws IOException {
        logger.info("GET request to download output-file for identifier={}", identifier);

        File file = taskService.getOutputFile(identifier);
        if (!file.exists()) {
            logger.warn("Output file not found for identifier={}", identifier);
            return ResponseEntity.notFound().build();
        }

        response.setContentType("application/x-download");
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());

        InputStream is = new FileInputStream(file);
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/task/{identifier}/ontology-file")
    public ResponseEntity<Void> getOntologyFile(@PathVariable String identifier, HttpServletResponse response) throws IOException {
        logger.info("GET request to download ontology for identifier={}", identifier);

        File file = taskService.getOntologyFile(identifier);
        if (!file.exists()) {
            logger.warn("Ontology not found for indentifier={}", identifier);
            return ResponseEntity.notFound().build();
        }

        response.setContentType("application/x-download");
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());

        InputStream is = new FileInputStream(file);
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
        return ResponseEntity.ok().build();
    }



    @GetMapping("/task/{identifier}/output-file/exists")
    public ResponseEntity<Boolean> getOutputFileExists(@PathVariable String identifier) {
        File f = taskService.getOutputFile(identifier);
        boolean exists = f.exists() && !f.isDirectory();
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/task/{identifier}/ontology-file/exists")
    public ResponseEntity<Boolean> getOntologyFileExists(@PathVariable String identifier) {
        File f = taskService.getOntologyFile(identifier);
        boolean exists = f.exists() && !f.isDirectory();
        return ResponseEntity.ok(exists);

    }


}
