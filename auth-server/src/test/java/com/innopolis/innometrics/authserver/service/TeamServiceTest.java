package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.TeamListRequest;
import com.innopolis.innometrics.authserver.dto.TeamRequest;
import com.innopolis.innometrics.authserver.entitiy.Company;
import com.innopolis.innometrics.authserver.entitiy.Project;
import com.innopolis.innometrics.authserver.entitiy.Team;
import com.innopolis.innometrics.authserver.repository.CompanyRepository;
import com.innopolis.innometrics.authserver.repository.ProjectRepository;
import com.innopolis.innometrics.authserver.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ValidationException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        TeamService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class TeamServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    TeamService service;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Test
    void existsByIDNegative() {
        boolean result = service.existsById(1);
        assertFalse(result);
    }

    @Test
    void existsByIDPositive() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        boolean result = service.existsById(1);
        assertTrue(result);
    }

    @Test
    void createTest() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        service.create(createTeamRequest());
        assertEquals(1, teamRepository.findAll().size());
    }

    @Test
    void updateNegative() {
        TeamRequest request = createTeamRequest();
        assertThrows(IllegalArgumentException.class, () -> service.update((request)));
    }

    @Test
    void updatePositive() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        TeamRequest request = createTeamRequest();
        assertNotNull(service.update((request)));
    }

    @Test
    void deleteNegative() {
        assertThrows(ValidationException.class, () -> service.delete(1));
    }

    @Test
    void deletePositive() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        service.delete(1);
        assertEquals(0, teamRepository.findAll().size());
    }

    @Test
    void findByTeamIdNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.findByTeamId(1));
    }

    @Test
    void findByTeamIdPositive() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        TeamRequest request = service.findByTeamId(1);
        assertEquals(1, request.getTeamId());
    }

    @Test
    void findTeamsByCompanyId() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        TeamListRequest request = service.findTeamsByCompanyId(1);
        assertNotNull(request.getTeamRequestList());
    }

    @Test
    void findTeamsByProjectId() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        TeamListRequest request = service.findTeamsByProjectId(1);
        assertNotNull(request.getTeamRequestList());
    }

    @Test
    void findTeamsByProjectIdAndCompanyId() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        TeamListRequest request = service.findTeamsByProjectIdAndCompanyId(1, 1);
        assertNotNull(request.getTeamRequestList());
    }

    @Test
    void findByTeamPropertiesNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.findByTeamProperties(1, 1, 1));
    }

    @Test
    void findByTeamPropertiesPositive() {
        projectRepository.save(createProject());
        companyRepository.save(createCompany());
        teamRepository.save(createTeam());
        TeamListRequest request = service.findByTeamProperties(1, 1, 1);
        assertNotNull(request.getTeamRequestList());
    }

    @Test
    void findAllActiveTeams() {
        TeamListRequest request = service.findAllActiveTeams();
        assertNotNull(request);
    }

    @Test
    void findAllTeams() {
        TeamListRequest request = service.findAllTeams();
        assertNotNull(request);
    }

    private Team createTeam() {
        Team team = new Team();
        team.setCreatedBy(TEST_DATA);
        team.setTeamId(1);
        team.setTeamName(TEST_DATA);
        team.setIsActive("Y");
        team.setDescription(TEST_DATA);
        team.setCompanyId(1);
        team.setProjectID(1);
        return team;
    }

    private TeamRequest createTeamRequest() {
        TeamRequest teamRequest = new TeamRequest();
        teamRequest.setCreatedBy(TEST_DATA);
        teamRequest.setTeamId(1);
        teamRequest.setTeamName(TEST_DATA);
        teamRequest.setIsActive("Y");
        teamRequest.setDescription(TEST_DATA);
        teamRequest.setCompanyId(1);
        teamRequest.setProjectID(1);
        return teamRequest;
    }

    private Company createCompany() {
        Company company = new Company();
        company.setCompanyId(1);
        company.setCreatedBy(TEST_DATA);
        company.setCreationDate(new Date());
        company.setCompanyName(TEST_DATA);
        company.setIsActive("Y");
        return company;
    }

    private Project createProject() {
        Project project = new Project();
        project.setProjectID(1);
        project.setCreatedBy(TEST_DATA);
        project.setCreationDate(new Date());
        project.setIsActive("Y");
        project.setName(TEST_DATA);
        return project;
    }
}
