package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentsXCompany;
import com.innopolis.innometrics.agentsgateway.repository.AgentsXCompanyRepository;
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
        AgentsXCompanyService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class AgentsXCompanyServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    AgentsXCompanyRepository repository;
    @Autowired
    AgentsXCompanyService service;

    @Test
    void getAgentsCompanyListNonExist() {
        List<AgentsXCompany> result = service.getAgentsCompanyList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsCompanyListExist() {
        repository.save(createAgentsXCompany());
        List<AgentsXCompany> result = service.getAgentsCompanyList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getAgentsCompanyByAgentIdNonExist() {
        List<AgentsXCompany> result = service.getAgentsCompanyByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsCompanyByAgentIdExist() {
        repository.save(createAgentsXCompany());
        List<AgentsXCompany> result = service.getAgentsCompanyByAgentId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getAgentsCompanyByCompanyIdNonExist() {
        List<AgentsXCompany> result = service.getAgentsCompanyByCompanyId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getAgentsCompanyByCompanyIdExist() {
        repository.save(createAgentsXCompany());
        List<AgentsXCompany> result = service.getAgentsCompanyByCompanyId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getAgentsCompanyByIdNonExist() {
        AgentsXCompany result = service.getAgentsCompanyById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getAgentsCompanyByIdExist() {
        repository.save(createAgentsXCompany());
        AgentsXCompany result = service.getAgentsCompanyById(1);
        Assertions.assertNotNull(result);
    }

    @Test
    void postResponseTest() {
        service.postAgentsCompany(createAgentsXCompany());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void putResponseTest() {
        service.putAgentsCompany(1, createAgentsXCompany());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void deleteAgentsCompanyByAgentIdNonExist() {
        List<AgentsXCompany> agentsXCompanies = service.deleteAgentsCompanyByAgentId(1);
        Assertions.assertEquals(0, agentsXCompanies.size());
    }

    @Test
    void deleteAgentsCompanyByAgentIdExist() {
        repository.save(createAgentsXCompany());
        List<AgentsXCompany> agentsXCompanies = service.deleteAgentsCompanyByAgentId(1);
        Assertions.assertEquals(1, agentsXCompanies.size());
    }

    @Test
    void deleteAgentsCompanyByCompanyIdNonExist() {
        List<AgentsXCompany> agentsXCompanies = service.deleteAgentsCompanyByCompanyId(1);
        Assertions.assertEquals(0, agentsXCompanies.size());
    }

    @Test
    void deleteAgentsCompanyByCompanyIdExist() {
        repository.save(createAgentsXCompany());
        List<AgentsXCompany> agentsXCompanies = service.deleteAgentsCompanyByCompanyId(1);
        Assertions.assertEquals(1, agentsXCompanies.size());
    }

    @Test
    void deleteAgentsCompanyByIdNonExist() {
        AgentsXCompany agentsXCompanies = service.deleteAgentsCompanyById(1);
        Assertions.assertNull(agentsXCompanies);
    }

    @Test
    void deleteAgentsCompanyByIdExist() {
        repository.save(createAgentsXCompany());
        AgentsXCompany agentsXCompanies = service.deleteAgentsCompanyById(1);
        Assertions.assertNotNull(agentsXCompanies);
    }


    private AgentsXCompany createAgentsXCompany() {
        AgentsXCompany agentsXCompany = new AgentsXCompany();
        agentsXCompany.setCompanyId(1);
        agentsXCompany.setAgentId(1);
        agentsXCompany.setCreatedBy(TEST_DATA);
        agentsXCompany.setKey(TEST_DATA);
        agentsXCompany.setToken(TEST_DATA);
        agentsXCompany.setIsActive("Y");
        agentsXCompany.setConfigid(1);
        agentsXCompany.setUpdateBy(TEST_DATA);
        return agentsXCompany;
    }
}
