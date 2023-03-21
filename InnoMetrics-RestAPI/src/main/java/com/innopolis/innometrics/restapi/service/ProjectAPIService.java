package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.entity.Project;
import com.innopolis.innometrics.restapi.entity.User;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.innopolis.innometrics.restapi.constants.ErrorMessages.PROJECT_DOES_NOT_EXIST;
import static com.innopolis.innometrics.restapi.constants.ErrorMessages.USER_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class ProjectAPIService {
    private final UserService userService;
    private final ProjectRepository projectRepository;

    public void makeUserProject(String projectName, String userEmail) {
        if (projectName == null || userEmail == null) {
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        }
        Project myProject = projectRepository.findByName(projectName);
        if (myProject == null) {
            throw new ValidationException(PROJECT_DOES_NOT_EXIST.getMessage());
        }
        User myUser = userService.findByEmail(userEmail);
        if (myUser == null) {
            throw new ValidationException(USER_DOES_NOT_EXIST.getMessage());
        }
        projectRepository.save(myProject);
    }
}
