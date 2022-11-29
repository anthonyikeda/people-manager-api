package com.example.basicapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectManager manager;

    public ProjectController(ProjectManager projectManager) {
        this.manager = projectManager;
    }

    @GetMapping(path="/{projectId}")
    public ResponseEntity<ProjectDAO> getProjectById(@PathVariable("projectId") Long projectId) {
        ProjectDAO project = this.manager.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }

    public ResponseEntity<List<ProjectDAO>> findPaginatedProjects(@RequestParam(name = "start", defaultValue = "0") Integer start,
                                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Long total = this.manager.countAllProjects();
        List<ProjectDAO> results = this.manager.findAllPaginated(start, size);

        return ResponseEntity.status(HttpStatus.OK).header("X-Total-Count", String.valueOf(total)).body(results);
    }
}
