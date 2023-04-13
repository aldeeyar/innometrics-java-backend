package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentConfigDetails;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.repository.AgentConfigDetailsRepository;
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
        AgentConfigDetailsService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentConfigDetailsServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentConfigDetailsRepository agentConfigDetailsRepository;
    @Autowired
    AgentConfigDetailsService service;

    @Test
    void getDetailsListNonExist() {
        List<AgentConfigDetails> result = service.getDetailsList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getDetailsListExist() {
        agentConfigDetailsRepository.save(createAgentConfigDetails());
        List<AgentConfigDetails> result = service.getDetailsList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getDetailsByMethodIdNonExist() {
        List<AgentConfigDetails> result = service.getDetailsByMethodId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getDetailsByMethodIdExist() {
        agentConfigDetailsRepository.save(createAgentConfigDetails());
        List<AgentConfigDetails> result = service.getDetailsByMethodId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getDetailsByIdNonExist() {
        AgentConfigDetails result = service.getDetailsById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getDetailsByIdIdExist() {
        agentConfigDetailsRepository.save(createAgentConfigDetails());
        AgentConfigDetails result = service.getDetailsById(1);
        Assertions.assertEquals(TEST_DATA, result.getParamName());
    }

    @Test
    void postDetails() {
        AgentConfigDetails result = service.postDetails(createAgentConfigDetails());
        Assertions.assertEquals(TEST_DATA, result.getParamName());
    }

    @Test
    void putDetails() {
        AgentConfigDetails result = service.putDetails(1, createAgentConfigDetails());
        Assertions.assertEquals(TEST_DATA, result.getParamName());
    }

    @Test
    void deleteDetailsByMethodIdNonExist() {
        List<AgentConfigDetails> result = service.deleteDetailsByMethodId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteDetailsByMethodIdExist() {
        agentConfigDetailsRepository.save(createAgentConfigDetails());
        List<AgentConfigDetails> result = service.deleteDetailsByMethodId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void deleteDetailsByIdNonExist() {
        AgentConfigDetails result = service.deleteDetailsById(1);
        Assertions.assertNull(result);
    }

    @Test
    void deleteDetailsByIdExist() {
        agentConfigDetailsRepository.save(createAgentConfigDetails());
        AgentConfigDetails result = service.deleteDetailsById(1);
        Assertions.assertEquals(TEST_DATA, result.getParamName());
    }

    private AgentConfigDetails createAgentConfigDetails() {
        AgentConfigDetails details = new AgentConfigDetails();
        details.setCreatedBy(TEST_DATA);
        details.setIsActive("Y");
        details.setParamName(TEST_DATA);
        details.setParamType(TEST_DATA);
        details.setAgentconfigmethods(createAgentConfigMethods());
        return details;
    }

    private AgentConfigMethods createAgentConfigMethods() {
        AgentConfigMethods methods = new AgentConfigMethods();
        methods.setOperation(TEST_DATA);
        methods.setCreatedBy(TEST_DATA);
        methods.setMethodId(1);
        return methods;
    }
}
