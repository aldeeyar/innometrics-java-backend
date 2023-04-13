package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.ReposXProject;
import com.innopolis.innometrics.agentsgateway.repository.ReposXProjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        ReposXProjectService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ReposXProjectServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    ReposXProjectService service;
    @Autowired
    ReposXProjectRepository repository;

    @Test
    void getExternalProjectTeamListNonExist() {
        List<ReposXProject> result = service.getReposProjectList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getExternalProjectTeamListExist() {
        repository.save(createReposXProject());
        List<ReposXProject> result = service.getReposProjectList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getReposProjectByAgentIdNonExist() {
        List<ReposXProject> result = service.getReposProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getReposProjectByAgentIdExist() {
        repository.save(createReposXProject());
        List<ReposXProject> result = service.getReposProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getReposProjectByProjectIdNonExist() {
        List<ReposXProject> result = service.getReposProjectByProjectId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getReposProjectByProjectIdExist() {
        repository.save(createReposXProject());
        List<ReposXProject> result = service.getReposProjectByProjectId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getReposProjectByIdNonExist() {
        ReposXProject result = service.getReposProjectById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getReposProjectByIdExist() {
        repository.save(createReposXProject());
        ReposXProject result = service.getReposProjectById(1);
        Assertions.assertNotNull(result);
    }

    @Test
    void postReposProjectTest() {
        service.postReposProject(createReposXProject());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void putReposProjectTest() {
        service.putReposProject(1, createReposXProject());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void deleteReposProjectByAgentIdNonExist() {
        List<ReposXProject> result = service.deleteReposProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteReposProjectByAgentIdExist() {
        repository.save(createReposXProject());
        List<ReposXProject> result = service.deleteReposProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteReposProjectByProjectIdNonExist() {
        List<ReposXProject> result = service.deleteReposProjectByProjectId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteReposProjectByProjectIdExist() {
        repository.save(createReposXProject());
        List<ReposXProject> result = service.deleteReposProjectByProjectId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void deleteReposProjectByIdNonExist() {
        ReposXProject result = service.deleteReposProjectById(1);
        Assertions.assertNull(result);
    }

    @Test
    void deleteReposProjectByIdExist() {
        repository.save(createReposXProject());
        ReposXProject result = service.deleteReposProjectById(1);
        Assertions.assertNotNull(result);
    }

    private ReposXProject createReposXProject() {
        ReposXProject reposXProject = new ReposXProject();
        reposXProject.setProjectId(1);
        reposXProject.setRepoId(TEST_DATA);
        reposXProject.setConfigId(1);
        reposXProject.setIsActive("Y");
        return reposXProject;
    }
}
