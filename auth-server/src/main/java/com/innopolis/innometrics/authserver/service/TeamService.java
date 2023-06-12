package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.TeamListRequest;
import com.innopolis.innometrics.authserver.dto.TeamRequest;
import com.innopolis.innometrics.authserver.entitiy.Team;
import com.innopolis.innometrics.authserver.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.List;

import static com.innopolis.innometrics.authserver.constants.ExceptionMessage.NO_TEAM_BY_ID_FOUND;
import static com.innopolis.innometrics.authserver.constants.ExceptionMessage.NO_TEAM_IN_PROJECT_FOUND;
import static com.innopolis.innometrics.authserver.service.PropertyNames.getNullPropertyNames;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public boolean existsById(Integer id){
        return teamRepository.existsByTeamId(id);
    }

    public TeamRequest create(TeamRequest detail) {
        Team entity = new Team();
        BeanUtils.copyProperties(detail, entity);
        entity = teamRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public TeamRequest update(TeamRequest detail) {
        Team entity = teamRepository.findByTeamId(detail.getTeamId());
        assertNotNull(entity, NO_TEAM_BY_ID_FOUND.getValue() + detail.getTeamId());
        detail.setTeamId(null);
        BeanUtils.copyProperties(detail, entity, getNullPropertyNames(detail));
        entity = teamRepository.saveAndFlush(entity);
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public void delete(Integer id) {
        Team entity = teamRepository.findById(id)
                .orElseThrow(() -> new ValidationException(NO_TEAM_BY_ID_FOUND.getValue() + id));
        teamRepository.delete(entity);
    }

    public TeamRequest findByTeamId(Integer id) {
        Team entity = teamRepository.findByTeamId(id);
        assertNotNull(entity, NO_TEAM_BY_ID_FOUND.getValue() + id);
        TeamRequest detail = new TeamRequest();
        BeanUtils.copyProperties(entity, detail);
        return detail;
    }

    public TeamListRequest findTeamsByCompanyId(Integer companyId) {
        List<Team> teamsFromCompany = teamRepository.findAllByCompanyId(companyId);
        assertNotNull(teamsFromCompany, "No teams found in this company " + companyId);
        return convertFromList(teamsFromCompany);
    }

    public TeamListRequest findTeamsByProjectId(Integer projectId) {
        List<Team> teamsFromProject = teamRepository.findAllByProjectID(projectId);
        assertNotNull(teamsFromProject, NO_TEAM_IN_PROJECT_FOUND.getValue() + projectId);
        return convertFromList(teamsFromProject);
    }

    public TeamListRequest findTeamsByProjectIdAndCompanyId(Integer projectId, Integer companyid) {
        List<Team> teamsFromProject = teamRepository.findAllByProjectIDAndCompanyId(projectId, companyid);
        assertNotNull(teamsFromProject,
                NO_TEAM_IN_PROJECT_FOUND.getValue() + projectId + " and from that company " + companyid);
        return convertFromList(teamsFromProject);
    }

    public TeamListRequest findByTeamProperties(Integer teamId, Integer companyId, Integer projectId) {
        TeamListRequest returnList = new TeamListRequest();
        if (teamId != null) {
            TeamRequest team = findByTeamId(teamId);
            assertNotNull(team, NO_TEAM_BY_ID_FOUND.getValue() + teamId);
            if (companyId != null) {
                if (projectId != null) {
                    if (team.getCompanyId().equals(companyId) && team.getProjectID().equals(projectId))
                        returnList.addTeamRequest(team);
                } else {
                    if (team.getCompanyId().equals(companyId))
                        returnList.addTeamRequest(team);
                }
            } else {
                if (projectId != null) {
                    if (team.getProjectID().equals(projectId))
                        returnList.addTeamRequest(team);
                } else {
                    returnList.addTeamRequest(findByTeamId(teamId));
                }
            }
        } else {
            if (companyId != null) {
                if (projectId != null) {
                    returnList = findTeamsByProjectIdAndCompanyId(projectId, companyId);
                } else {
                    returnList = findTeamsByCompanyId(companyId);
                }
            } else {
                returnList = findTeamsByProjectId(projectId);
            }
        }
        return returnList;
    }

    public TeamListRequest findAllActiveTeams(){
        List<Team> activeTeams = teamRepository.findAllActive();
        assertNotNull(activeTeams,
                "No active teams found " );
        return convertFromList(activeTeams);
    }

    public TeamListRequest findAllTeams(){
        List<Team> allTeams = teamRepository.findAll();
        assertNotNull(allTeams,
                "No teams found " );
        return convertFromList(allTeams);
    }

    private TeamListRequest convertFromList(List<Team> teamList){
        TeamListRequest teamListRequest = new TeamListRequest();
        for (Team activeTeam : teamList) {
            TeamRequest detail = new TeamRequest();
            BeanUtils.copyProperties(activeTeam,detail);
            teamListRequest.addTeamRequest(detail);
        }
        return teamListRequest;
    }
}
