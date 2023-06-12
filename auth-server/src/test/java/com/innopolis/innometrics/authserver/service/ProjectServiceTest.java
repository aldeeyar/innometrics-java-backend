package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.ProjectListRequest;
import com.innopolis.innometrics.authserver.dto.ProjectRequest;
import com.innopolis.innometrics.authserver.entitiy.Project;
import com.innopolis.innometrics.authserver.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ValidationException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        ProjectService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ProjectServiceTest {
    private final static String TEST_DATA = "Test";

    @Autowired
    ProjectService service;
    @Autowired
    ProjectRepository projectRepository;

    @Test
    void existsByProjectIDNegative() {
        Boolean result = service.existsByProjectID(1);
        assertFalse(result);
    }

    @Test
    void existsByProjectIDPositive() {
        projectRepository.save(createProject());
        Boolean result = service.existsByProjectID(1);
        assertTrue(result);
    }

    @Test
    void create() {
        service.create(createProjectRequest());
        assertEquals(1, projectRepository.findAll().size());
    }

    @Test
    void getByIdNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.getById(1));
    }

    @Test
    void getByIdPositive() {
        projectRepository.save(createProject());
        ProjectRequest request = service.getById(1);
        assertEquals(1, request.getProjectID());
    }

    @Test
    void updateNegative() {
        ProjectRequest request = createProjectRequest();
        assertThrows(IllegalArgumentException.class, () -> service.update(request));
    }

    @Test
    void updatePositive() {
        projectRepository.save(createProject());
        ProjectRequest request = createProjectRequest();
        service.update(request);
        assertEquals(1, request.getProjectID());
    }

    @Test
    void getActiveProjectList() {
        ProjectListRequest request = service.getActiveProjectList();
        assertNotNull(request);
    }

    @Test
    void getAllProjectList() {
        ProjectListRequest request = service.getAllProjectList();
        assertNotNull(request);
    }

    @Test
    void deleteNegative() {
        assertThrows(ValidationException.class, () -> service.delete(1));
    }

    @Test
    void deletePositive() {
        projectRepository.save(createProject());
        service.delete(1);
        assertEquals(0, projectRepository.findAll().size());
    }

    private Project createProject() {
        Project project = new Project();
        project.setProjectID(1);
        project.setCreatedBy(TEST_DATA);
        project.setCreationDate(new Date());
        project.setIsActive("Y");
        project.setName(TEST_DATA);
        return project;
    }

    private ProjectRequest createProjectRequest() {
        ProjectRequest request = new ProjectRequest();
        request.setProjectID(1);
        request.setName(TEST_DATA);
        request.setIsActive("Y");
        return request;
    }
}
