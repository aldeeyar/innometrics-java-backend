package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.entity.Project;
import com.innopolis.innometrics.restapi.entity.User;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.repository.ProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        ProjectAPIService.class,
        UserService.class,
        RoleService.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectAPIServiceTest {
    @MockBean
    UserService userService;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectAPIService projectAPIService;

    @Test
    void makeUserProjectNullDataTest() {
        Assertions.assertThrows(ValidationException.class,
                () -> projectAPIService.makeUserProject(null, null));
    }

    @Test
    void makeUserProjectNotNullDataEmptyRepositoryTest() {
        Assertions.assertThrows(ValidationException.class,
                () -> projectAPIService.makeUserProject("testName", "test@mail.ru"));
    }

    @Test
    void makeUserProjectNotNullDataExistRepositoryIncorrectEmailTest() {
        Project project = makeProject();
        projectRepository.save(project);
        Mockito.when(userService.findByEmail("test@mail.ru")).thenReturn(null);
        Assertions.assertThrows(ValidationException.class,
                () -> projectAPIService.makeUserProject("testName", "test@mail.ru"));
    }

    @Test
    void makeUserProjectNotNullDataExistRepositoryCorrectEmailTest() {
        Project project = makeProject();
        projectRepository.save(project);
        Mockito.when(userService.findByEmail("test@mail.ru")).thenReturn(new User());
        projectAPIService.makeUserProject("testName", "test@mail.ru");
        Assertions.assertEquals(1, projectRepository.findAll().size());
    }

    private Project makeProject() {
        Project project = new Project();
        project.setCreatedBy("Dariya");
        project.setName("testName");
        project.setIsActive("Y");
        return project;
    }
}
