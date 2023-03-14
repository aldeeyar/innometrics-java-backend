package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.DTO.ProjectListRequest;
import com.innopolis.innometrics.restapi.DTO.ProjectRequest;
import com.innopolis.innometrics.restapi.DTO.UserListResponse;
import com.innopolis.innometrics.restapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AdminAPI {
    private final AdminService adminService;

    @GetMapping("/Users")
    public ResponseEntity<UserListResponse> getActiveUsers(@RequestParam(required = false) String projectId) {
        UserListResponse response = adminService.getActiveUsers(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Users/projects/{userName}")
    public ResponseEntity<ProjectListRequest> getProjectsByUsername(@PathVariable String userName,
                                                                    @RequestHeader(required = false) String token) {
        ProjectListRequest response = adminService.getProjectsByUsername(userName);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/Project")
    public ResponseEntity<ProjectRequest> createProject(@RequestBody ProjectRequest project,
                                                        @RequestHeader(required = false) String token) {
        project.setProjectID(null);
        ProjectRequest response = adminService.createProject(project, token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/Project/{id}")
    public ResponseEntity<ProjectRequest> updateProject(@PathVariable Integer id,
                                                        @RequestBody ProjectRequest project,
                                                        @RequestHeader(required = false) String token) {
        project.setProjectID(id);
        ProjectRequest response = adminService.updateProject(project, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Project/{id}")
    public ResponseEntity<ProjectRequest> getProjectById(@PathVariable int id) {
        ProjectRequest response = adminService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Project/active")
    public ResponseEntity<ProjectListRequest> getActiveProjects() {
        ProjectListRequest response = adminService.getActiveProjects();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Project/all")
    public ResponseEntity<ProjectListRequest> getAllProjects() {
        ProjectListRequest response = adminService.getAllProjects();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/Project")
    public ResponseEntity<ProjectRequest> deleteProject(@RequestParam Integer id,
                                                        @RequestHeader(required = false) String token) {
        adminService.deleteProject(id, token);
        return ResponseEntity.ok().build();
    }
}