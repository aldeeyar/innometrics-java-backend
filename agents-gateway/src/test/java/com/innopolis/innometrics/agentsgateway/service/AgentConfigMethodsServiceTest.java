package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigMethodsRepository;
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
        AgentConfigMethodsService.class,
        AgentConfigDetailsService.class,
        AgentResponseConfigService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentConfigMethodsServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentConfigMethodsRepository agentconfigmethodsRepository;
    @Autowired
    AgentConfigDetailsService agentconfigdetailsService;
    @Autowired
    AgentResponseConfigService agentresponseconfigService;
    @Autowired
    AgentConfigMethodsService service;

    @Test
    void getMethodsListNonExist() {
        List<AgentConfigMethods> result = service.getMethodsList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getMethodsListExist() {
        agentconfigmethodsRepository.save(createAgentConfigMethods());
        List<AgentConfigMethods> result = service.getMethodsList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getMethodsByAgentIdNonExist() {
        List<AgentConfigMethods> result = service.getMethodsByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getMethodsByAgentIdExist() {
        agentconfigmethodsRepository.save(createAgentConfigMethods());
        List<AgentConfigMethods> result = service.getMethodsByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getMethodByIdNonExist() {
        AgentConfigMethods result = service.getMethodById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getMethodByIdExist() {
        agentconfigmethodsRepository.save(createAgentConfigMethods());
        AgentConfigMethods result = service.getMethodById(1);
        Assertions.assertEquals(TEST_DATA, result.getOperation());
    }

    @Test
    void getMethodsByAgentidAndOperationIdNonExist() {
        AgentConfigMethods result = service.getMethodsByAgentidAndOperation(1, TEST_DATA);
        Assertions.assertNull(result);
    }

    @Test
    void postMethod() {
        AgentConfigMethods result = service.postMethod(createAgentConfigMethods());
        Assertions.assertEquals(TEST_DATA, result.getOperation());
    }

    @Test
    void putMethod() {
        AgentConfigMethods result = service.putMethod(1, createAgentConfigMethods());
        Assertions.assertEquals(TEST_DATA, result.getOperation());
    }

    @Test
    void deleteMethodsByAgentIdNonExist() {
        List<AgentConfigMethods> result = service.deleteMethodsByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteMethodsByAgentExist() {
        agentconfigmethodsRepository.save(createAgentConfigMethods());
        List<AgentConfigMethods> result = service.deleteMethodsByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteMethodByIdNonExist() {
        AgentConfigMethods result = service.deleteMethodById(1);
        Assertions.assertNull(result);
    }

    @Test
    void deleteMethodByIdExist() {
        agentconfigmethodsRepository.save(createAgentConfigMethods());
        AgentConfigMethods result = service.deleteMethodById(1);
        Assertions.assertEquals(TEST_DATA, result.getEndpoint());
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
