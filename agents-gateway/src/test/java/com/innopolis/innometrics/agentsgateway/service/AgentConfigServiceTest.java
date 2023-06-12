package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.dto.AgentConfigResponse;
import com.innopolis.innometrics.agentsgateway.dto.AgentListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigMethodsRepository;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        AgentConfigService.class,
        AgentDataConfigService.class,
        AgentConfigMethodsService.class,
        ReposXProjectService.class,
        AgentsXProjectService.class,
        ExternalProjectXTeamService.class,
        AgentsXCompanyService.class,
        AgentConfigDetailsService.class,
        AgentResponseConfigService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentConfigServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentConfigService service;
    @Autowired
    AgentConfigRepository agentconfigRepository;
    @Autowired
    AgentConfigMethodsRepository agentconfigmethodsRepository;
    @Autowired
    AgentDataConfigService agentdataconfigService;
    @Autowired
    AgentConfigMethodsService agentconfigmethodsService;
    @Autowired
    ReposXProjectService reposxprojectService;
    @Autowired
    AgentsXProjectService agentsxprojectService;
    @Autowired
    ExternalProjectXTeamService externalprojectxteamService;
    @Autowired
    AgentsXCompanyService agentsxcompanyService;

    @Test
    void getAgentByIdNonExist() {
        AgentConfig result = service.getAgentById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getAgentByIdExist() {
        agentconfigRepository.save(createAgentConfig());
        AgentConfig result = service.getAgentById(1);
        Assertions.assertEquals(TEST_DATA, result.getAgentName());
    }

    @Test
    void postAgentTest() {
        service.postAgent(createAgentConfig());
        Assertions.assertEquals(1, agentconfigRepository.findAll().size());
    }

    @Test
    void putAgentTest() {
        service.putAgent(1, createAgentConfig());
        Assertions.assertEquals(1, agentconfigRepository.findAll().size());
    }

    @Test
    void deleteAgentByIdNonExist() {
        AgentConfig agentConfig = service.deleteAgentById(1);
        Assertions.assertNull(agentConfig);
    }

    @Test
    void deleteAgentByIdExist() {
        agentconfigRepository.save(createAgentConfig());
        AgentConfig agentConfig = service.deleteAgentById(1);
        Assertions.assertNotNull(agentConfig);
    }

    @Test
    void getAgentConfigTestNonExist() {
        AgentConfigResponse response = service.getAgentConfig(1, TEST_DATA);
        Assertions.assertNull(response.getMethodsConfig());
    }

    @Test
    void getAgentConfigTestExist() {
        agentconfigmethodsRepository.save(createAgentConfigMethods());
        AgentConfigResponse response = service.getAgentConfig(1, TEST_DATA);
        Assertions.assertNull(response.getMethodsConfig());
    }

    @Test
    void getAgentListTestNonExist() {
        AgentListResponse response = service.getAgentList(1);
        Assertions.assertNotNull(response.getAgentList());
    }

    @Test
    void getAgentListTestExist() {
        agentconfigRepository.save(createAgentConfig());
        AgentListResponse response = service.getAgentList(1);
        Assertions.assertNotNull(response);
    }

    private AgentConfig createAgentConfig() {
        AgentConfig agentConfig = new AgentConfig();
        agentConfig.setAgentId(1);
        agentConfig.setAgentName(TEST_DATA);
        agentConfig.setCreatedBy(TEST_DATA);
        agentConfig.setDescription(TEST_DATA);
        agentConfig.setIsActive("Y");
        return agentConfig;
    }

    private AgentConfigMethods createAgentConfigMethods() {
        AgentConfigMethods methods = new AgentConfigMethods();
        methods.setMethodId(1);
        methods.setCreatedBy(TEST_DATA);
        methods.setAgentId(1);
        methods.setOperation(TEST_DATA);
        methods.setEndpoint(TEST_DATA);
        methods.setIsActive("Y");
        return methods;
    }
}
