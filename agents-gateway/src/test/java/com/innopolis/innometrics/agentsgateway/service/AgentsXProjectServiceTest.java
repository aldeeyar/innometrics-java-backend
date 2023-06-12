package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentsXProject;
import com.innopolis.innometrics.agentsgateway.repository.AgentsXProjectRepository;
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
        AgentsXProjectService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentsXProjectServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentsXProjectRepository repository;
    @Autowired
    AgentsXProjectService service;

    @Test
    void getAgentsProjectListNonExist() {
        List<AgentsXProject> result = service.getAgentsProjectList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsProjectListExist() {
        repository.save(createAgentsXProject());
        List<AgentsXProject> result = service.getAgentsProjectList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getAgentsProjectByAgentIdNonExist() {
        List<AgentsXProject> result = service.getAgentsProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsProjectByAgentIdExist() {
        repository.save(createAgentsXProject());
        List<AgentsXProject> result = service.getAgentsProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsProjectByProjectIdNonExist() {
        List<AgentsXProject> result = service.getAgentsProjectByProjectId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsProjectByProjectIdExist() {
        repository.save(createAgentsXProject());
        List<AgentsXProject> result = service.getAgentsProjectByProjectId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getAgentsProjectByAgentIdAndProjectIdNonExist() {
        AgentsXProject result = service.getAgentsProjectByAgentIdAndProjectId(1, 1);
        Assertions.assertNull(result);
    }

    @Test
    void getAgentsProjectByAgentIdAndProjectIdExist() {
        repository.save(createAgentsXProject());
        AgentsXProject result = service.getAgentsProjectByAgentIdAndProjectId(1, 1);
        Assertions.assertNull(result);
    }

    @Test
    void getAgentsProjectByIdNonExist() {
        AgentsXProject result = service.getAgentsProjectById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getAgentsProjectByIdExist() {
        repository.save(createAgentsXProject());
        AgentsXProject result = service.getAgentsProjectById(1);
        Assertions.assertNotNull(result);
    }

    @Test
    void postResponseTest() {
        service.postAgentsProject(createAgentsXProject());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void putResponseTest() {
        service.putAgentsProject(1, createAgentsXProject());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void deleteAgentsProjectByAgentIdNonExist() {
        List<AgentsXProject> result = service.deleteAgentsProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteAgentsProjectByAgentIdExist() {
        repository.save(createAgentsXProject());
        List<AgentsXProject> result = service.deleteAgentsProjectByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteAgentsProjectByProjectIdNonExist() {
        List<AgentsXProject> result = service.deleteAgentsProjectByProjectId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteAgentsProjectByProjectIdExist() {
        repository.save(createAgentsXProject());
        List<AgentsXProject> result = service.deleteAgentsProjectByProjectId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void deleteAgentsProjectByIdNonExist() {
        AgentsXProject result = service.deleteAgentsProjectById(1);
        Assertions.assertNull(result);
    }

    @Test
    void deleteAgentsProjectByIdExist() {
        repository.save(createAgentsXProject());
        AgentsXProject result = service.deleteAgentsProjectById(1);
        Assertions.assertNotNull(result);
    }

    private AgentsXProject createAgentsXProject() {
        AgentsXProject agentsXProject = new AgentsXProject();
        agentsXProject.setProjectId(1);
        agentsXProject.setCreatedBy(TEST_DATA);
        agentsXProject.setToken(TEST_DATA);
        agentsXProject.setKey(TEST_DATA);
        agentsXProject.setIsActive("Y");
        agentsXProject.setConfigId(1);
        return agentsXProject;
    }

}
