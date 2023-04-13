package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.ExternalProjectXTeam;
import com.innopolis.innometrics.agentsgateway.repository.ExternalProjectXTeamRepository;
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
        ExternalProjectXTeamService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class ExternalProjectXTeamServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    ExternalProjectXTeamService service;
    @Autowired
    ExternalProjectXTeamRepository repository;

    @Test
    void getExternalProjectTeamListNonExist() {
        List<ExternalProjectXTeam> result = service.getExternalProjectTeamList();
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getExternalProjectTeamListExist() {
        repository.save(createExternalProjectXTeam());
        List<ExternalProjectXTeam> result = service.getExternalProjectTeamList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getExternalProjectTeamByAgentIdNonExist() {
        List<ExternalProjectXTeam> result = service.getExternalProjectTeamByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getExternalProjectTeamByAgentIdExist() {
        repository.save(createExternalProjectXTeam());
        List<ExternalProjectXTeam> result = service.getExternalProjectTeamByAgentId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getExternalProjectTeamByTeamIdNonExist() {
        List<ExternalProjectXTeam> result = service.getExternalProjectTeamByTeamId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void getExternalProjectTeamByTeamIdExist() {
        repository.save(createExternalProjectXTeam());
        List<ExternalProjectXTeam> result = service.getExternalProjectTeamByTeamId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void getExternalProjectTeamByIdNonExist() {
        ExternalProjectXTeam result = service.getExternalProjectTeamById(1);
        Assertions.assertNull(result);
    }

    @Test
    void getExternalProjectTeamByIdExist() {
        repository.save(createExternalProjectXTeam());
        ExternalProjectXTeam result = service.getExternalProjectTeamById(1);
        Assertions.assertNotNull(result);
    }

    @Test
    void postResponseTest() {
        service.postExternalProjectTeam(createExternalProjectXTeam());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void putResponseTest() {
        service.putExternalProjectTeam(1, createExternalProjectXTeam());
        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void deleteExternalProjectTeamByAgentIdNonExist() {
        List<ExternalProjectXTeam> result = service.deleteExternalProjectTeamByAgentId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteExternalProjectTeamByAgentIdExist() {
        repository.save(createExternalProjectXTeam());
        List<ExternalProjectXTeam> result = service.deleteExternalProjectTeamByAgentId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void deleteExternalProjectTeamByTeamIdNonExist() {
        List<ExternalProjectXTeam> result = service.deleteExternalProjectTeamByTeamId(1);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void deleteExternalProjectTeamByTeamIdExist() {
        repository.save(createExternalProjectXTeam());
        List<ExternalProjectXTeam> result = service.deleteExternalProjectTeamByTeamId(1);
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void deleteExternalProjectTeamByIdNonExist() {
        ExternalProjectXTeam result = service.deleteExternalProjectTeamById(1);
        Assertions.assertNull(result);
    }

    @Test
    void deleteExternalProjectTeamByIdExist() {
        repository.save(createExternalProjectXTeam());
        ExternalProjectXTeam result = service.deleteExternalProjectTeamById(1);
        Assertions.assertNotNull(result);
    }

    private ExternalProjectXTeam createExternalProjectXTeam() {
        ExternalProjectXTeam externalProjectXTeam = new ExternalProjectXTeam();
        externalProjectXTeam.setTeamId(1);
        externalProjectXTeam.setConfigId(1);
        externalProjectXTeam.setRepoId(TEST_DATA);
        externalProjectXTeam.setIsActive("Y");
        externalProjectXTeam.setAgentId(1);
        return externalProjectXTeam;
    }
}
