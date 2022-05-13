package nl.um.cds.triplifierboot.rest.controller;

import nl.um.cds.triplifierboot.rest.controller.dto.TaskDto;
import nl.um.cds.triplifierboot.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public TaskDto newTask(@RequestBody TaskDto task) {
        return taskService.save(task);
    }

    @PostMapping("/binary")
    public ResponseEntity<TaskDto> createTask(@RequestParam("file") MultipartFile file) throws IOException {
        TaskDto task = taskService.createTask(file);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/task/{id}/output-file")
    public ResponseEntity<Resource> getOutputFile(String taskId) throws IOException {
        File file = taskService.getOntologyFile(taskId);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/task/{id}/ontology-file")
    public ResponseEntity<Resource> getOntologyFile(String taskId) throws IOException {

        File file = taskService.getOntologyFile(taskId);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/task/{id}/outputfile-file/exists")
    public ResponseEntity<Boolean> getOutputFileExists(String taskId) throws IOException {
        File f = taskService.getOutputFile(taskId);
        boolean exists = f.exists() && !f.isDirectory();
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/task/{id}/ontology-file/exists")
    public ResponseEntity<Boolean> getOntologyFileExists(String taskId) throws IOException {
        File f = taskService.getOntologyFile(taskId);
        boolean exists = f.exists() && !f.isDirectory();
        return ResponseEntity.ok(exists);
    }


}
