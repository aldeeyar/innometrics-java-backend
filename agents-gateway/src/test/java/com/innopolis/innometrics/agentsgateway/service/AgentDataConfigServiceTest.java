package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentDataConfig;
import com.innopolis.innometrics.agentsgateway.repository.AgentDataConfigRepository;
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
        AgentDataConfigService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentDataConfigServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentDataConfigService service;
    @Autowired
    AgentDataConfigRepository agentdataconfigRepository;

    @Test
    void getDataListNonExist() {
        List<AgentDataConfig> response = service.getDataList();
        Assertions.assertEquals(0, response.size());
    }

    @Test
    void getDataListExist() {
        agentdataconfigRepository.save(createAgentDataConfig());
        List<AgentDataConfig> response = service.getDataList();
        Assertions.assertEquals(1, response.size());
    }

    @Test
    void getDataAgentsByAgentIdNonExist() {
        List<AgentDataConfig> response = service.getDataAgentsByAgentId(1);
        Assertions.assertEquals(0, response.size());
    }

    @Test
    void getDataAgentsByAgentIdExist() {
        agentdataconfigRepository.save(createAgentDataConfig());
        List<AgentDataConfig> response = service.getDataAgentsByAgentId(1);
        Assertions.assertEquals(1, response.size());
    }

    @Test
    void getDataByIdNonExist() {
        AgentDataConfig response = service.getDataById(1);
        Assertions.assertNull(response);
    }

    @Test
    void getDataByIdExist() {
        agentdataconfigRepository.save(createAgentDataConfig());
        AgentDataConfig response = service.getDataById(1);
        Assertions.assertNotNull(response);
    }

    @Test
    void postDataTest() {
        service.postData(createAgentDataConfig());
        Assertions.assertEquals(1, agentdataconfigRepository.findAll().size());
    }

    @Test
    void putDataTest() {
        service.putData(1, createAgentDataConfig());
        Assertions.assertEquals(1, agentdataconfigRepository.findAll().size());
    }

    @Test
    void deleteDataByAgentIdNonExistTest() {
        List<AgentDataConfig> data = service.deleteDataByAgentId(1);
        Assertions.assertEquals(0, data.size());
    }

    @Test
    void deleteDataByAgentIdExistTest() {
        agentdataconfigRepository.save(createAgentDataConfig());
        List<AgentDataConfig> data = service.deleteDataByAgentId(1);
        Assertions.assertEquals(1, data.size());
    }

    @Test
    void deleteDataByIdNonExist() {
        AgentDataConfig config = service.deleteDataById(1);
        Assertions.assertNull(config);
    }

    @Test
    void deleteDataByIdExist() {
        agentdataconfigRepository.save(createAgentDataConfig());
        AgentDataConfig config = service.deleteDataById(1);
        Assertions.assertNotNull(config);
    }

    private AgentDataConfig createAgentDataConfig() {
        AgentDataConfig config = new AgentDataConfig();
        config.setCreatedBy(TEST_DATA);
        config.setIsActive("Y");
        config.setDataConfigId(1);
        config.setAgentId(1);
        config.setTableName(TEST_DATA);
        config.setEventType(TEST_DATA);
        config.setEventDateField(TEST_DATA);
        config.setEventAuthorField(TEST_DATA);
        return config;
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
}
