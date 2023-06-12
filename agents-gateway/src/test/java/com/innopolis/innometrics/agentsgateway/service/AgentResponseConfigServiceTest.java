package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentResponseConfig;
import com.innopolis.innometrics.agentsgateway.repository.AgentResponseConfigRepository;
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
        AgentResponseConfigService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentResponseConfigServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentResponseConfigRepository repository;
    @Autowired
    AgentResponseConfigService service;

    @Test
    void getResponsesListTestNonExist() {
        List<AgentResponseConfig> configs = service.getResponsesList();
        Assertions.assertEquals(0, configs.size());
    }

    @Test
    void getResponsesListTestExist() {
        repository.save(createAgentResponseConfig());
        List<AgentResponseConfig> configs = service.getResponsesList();
        Assertions.assertEquals(1, configs.size());
    }

    @Test
    void getResponsesByMethodIdNonExist() {
        List<AgentResponseConfig> configs = service.getResponsesByMethodId(1);
        Assertions.assertEquals(0, configs.size());
    }

    @Test
    void getResponsesByMethodIdExist() {
        repository.save(createAgentResponseConfig());
        List<AgentResponseConfig> configs = service.getResponsesByMethodId(1);
        Assertions.assertEquals(0, configs.size());
    }

    @Test
    void getResponseByIdNonExist() {
        AgentResponseConfig config = service.getResponseById(1);
        Assertions.assertNull(config);
    }

    @Test
    void getResponseByIdExist() {
        repository.save(createAgentResponseConfig());
        AgentResponseConfig config = service.getResponseById(1);
        Assertions.assertNotNull(config);
    }

    @Test
    void postResponseTest() {
        service.postResponse(createAgentResponseConfig());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void putResponseTest() {
        service.putResponse(1, createAgentResponseConfig());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void deleteResponseByMethodIdNonExist() {
        List<AgentResponseConfig> configs = service.deleteResponseByMethodId(1);
        Assertions.assertEquals(0, configs.size());
    }

    @Test
    void deleteResponseByMethodIdExist() {
        repository.save(createAgentResponseConfig());
        List<AgentResponseConfig> configs = service.deleteResponseByMethodId(1);
        Assertions.assertEquals(0, configs.size());
    }

    @Test
    void deleteResponseByIdNonExist() {
        AgentResponseConfig config = service.deleteResponseById(1);
        Assertions.assertNull(config);
    }

    @Test
    void deleteResponseByIdExist() {
        repository.save(createAgentResponseConfig());
        AgentResponseConfig config = service.deleteResponseById(1);
        Assertions.assertNotNull(config);
    }

    private AgentResponseConfig createAgentResponseConfig() {
        AgentResponseConfig config = new AgentResponseConfig();
        config.setCreatedBy(TEST_DATA);
        config.setIsActive("Y");
        config.setParamName(TEST_DATA);
        config.setConfigResponseId(1);
        config.setParamType(TEST_DATA);
        config.setResponseParam(TEST_DATA);
        return config;
    }
}
