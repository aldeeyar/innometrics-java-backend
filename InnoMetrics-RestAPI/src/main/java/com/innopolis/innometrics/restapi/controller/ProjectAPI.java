package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.entity.Project;
import com.innopolis.innometrics.restapi.entity.User;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.repository.ProjectRepository;
import com.innopolis.innometrics.restapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.innopolis.innometrics.restapi.constants.ErrorMessages.PROJECT_DOES_NOT_EXIST;
import static com.innopolis.innometrics.restapi.constants.ErrorMessages.USER_DOES_NOT_EXIST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProjectAPI {
    private final UserService userService;
    private final ProjectRepository projectService;

    @PostMapping("/project/{projectName}")
    public ResponseEntity<Boolean> inviteUserProject(@PathVariable String projectName,
                                                     @RequestParam String userEmail,
                                                     @RequestParam Boolean manager,
                                                     @RequestHeader(required = false) String token) {
        if (projectName == null || userEmail == null) {
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        }
        Project myProject = projectService.findByName(projectName);
        if (myProject == null) {
            throw new ValidationException(PROJECT_DOES_NOT_EXIST.getMessage());
        }
        User myUser = userService.findByEmail(userEmail);
        if (myUser == null) {
            throw new ValidationException(USER_DOES_NOT_EXIST.getMessage());
        }
        projectService.save(myProject);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
