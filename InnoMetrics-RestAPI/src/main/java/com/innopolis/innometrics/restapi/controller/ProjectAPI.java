package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.service.ProjectAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectAPI {
    private final ProjectAPIService service;

    @PostMapping("/project/{projectName}")
    public ResponseEntity<Boolean> inviteUserProject(@PathVariable String projectName,
                                                     @RequestParam String userEmail,
                                                     @RequestParam Boolean manager,
                                                     @RequestHeader(required = false) String token) {
        service.makeUserProject(projectName, userEmail);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
