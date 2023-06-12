package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.dto.ProjectListRequest;
import com.innopolis.innometrics.authserver.dto.ProjectRequest;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminAPI/Project")
@RequiredArgsConstructor
public class ProjectAPI {
    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectRequest> createProject(@RequestBody ProjectRequest project,
                                                        @RequestHeader(required = false) String token) {
        if (project == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        if (project.getName() == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        ProjectRequest response = projectService.create(project);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectRequest> updateProject(@PathVariable Integer id,
                                                        @RequestBody ProjectRequest project,
                                                        @RequestHeader(required = false) String token) {
        if (project == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        project.setProjectID(id);
        ProjectRequest response;
        if (Boolean.FALSE.equals(projectService.existsByProjectID(project.getProjectID()))) {
            response = projectService.create(project);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            response = projectService.update(project);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<ProjectListRequest> getActiveProjects() {
        ProjectListRequest response = projectService.getActiveProjectList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<ProjectListRequest> getAllProjects() {
        ProjectListRequest response = projectService.getAllProjectList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectRequest> getProjectById(@PathVariable int id) {
        if (Boolean.TRUE.equals(projectService.existsByProjectID(id))) {
            ProjectRequest response = projectService.getById(id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<ProjectRequest> deleteProject(@RequestParam Integer id,
                                                        @RequestHeader(required = false) String token) {
        if (id == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        try {
            projectService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new ValidationException(
                    "Entries from 'repos_x_project' and 'agents_x_project' tables with such projectid must be deleted firstly"
            );
        }
    }
}
