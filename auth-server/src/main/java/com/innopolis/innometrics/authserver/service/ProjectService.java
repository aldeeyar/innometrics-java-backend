package com.innopolis.innometrics.authserver.service;


import com.innopolis.innometrics.authserver.dto.ProjectListRequest;
import com.innopolis.innometrics.authserver.dto.ProjectRequest;
import com.innopolis.innometrics.authserver.entitiy.Project;
import com.innopolis.innometrics.authserver.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

import static com.innopolis.innometrics.authserver.constants.ExceptionMessage.NO_PROJECT_FOUND;
import static com.innopolis.innometrics.authserver.service.PropertyNames.getNullPropertyNames;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository repository;

    public Boolean existsByProjectID(Integer projectId) {
        return repository.existsByProjectID(projectId);
    }

    public ProjectRequest create(ProjectRequest detail) {
        Project entity = new Project();
        detail.setProjectID(null);
        BeanUtils.copyProperties(detail, entity, getNullPropertyNames(detail));
        entity = repository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;

    }

    public ProjectRequest getById(Integer projectId) {
        Project project = repository.findByProjectID(projectId);
        ProjectRequest detail = new ProjectRequest();
        BeanUtils.copyProperties(project, detail);
        return detail;
    }

    public ProjectRequest update(ProjectRequest detail) {
        Project entity = repository.findByProjectID(detail.getProjectID());
        assertNotNull(entity, NO_PROJECT_FOUND.getValue() + detail.getProjectID());
        detail.setProjectID(null);
        BeanUtils.copyProperties(detail, entity, getNullPropertyNames(detail));
        entity = repository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public ProjectListRequest getActiveProjectList() {
        List<Project> result = repository.findAllActive();
        return convertFromList(result);
    }

    public ProjectListRequest getAllProjectList() {
        List<Project> result = repository.findAll();
        return convertFromList(result);
    }

    private ProjectListRequest convertFromList(List<Project> projectList) {
        ProjectListRequest response = new ProjectListRequest();
        for (Project p : projectList) {
            ProjectRequest detail = new ProjectRequest();
            BeanUtils.copyProperties(p, detail);
            response.getProjectList().add(detail);
        }
        return response;
    }

    public void delete(Integer projectId) {
        Project entity = repository.findById(projectId)
                .orElseThrow(() -> new ValidationException(NO_PROJECT_FOUND.getValue() + projectId));
        repository.delete(entity);
    }
}
