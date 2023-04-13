package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.ExternalProjectXTeam;
import com.innopolis.innometrics.agentsgateway.repository.ExternalProjectXTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExternalProjectXTeamService {
    private final ExternalProjectXTeamRepository externalprojectxteamRepository;

    public List<ExternalProjectXTeam> getExternalProjectTeamList() {
        return externalprojectxteamRepository.findAll();
    }

    public List<ExternalProjectXTeam> getExternalProjectTeamByAgentId(Integer agentId) {
        List<ExternalProjectXTeam> externalProjectTeamList = externalprojectxteamRepository.findAll();
        List<ExternalProjectXTeam> result = new ArrayList<>();
        for (ExternalProjectXTeam externalprojectxteam : externalProjectTeamList) {
            if (externalprojectxteam.getAgentId().equals(agentId)) {
                result.add(externalprojectxteam);
            }
        }
        return result;
    }

    public List<ExternalProjectXTeam> getExternalProjectTeamByTeamId(Integer teamId) {
        List<ExternalProjectXTeam> externalProjectTeamList = externalprojectxteamRepository.findAll();
        List<ExternalProjectXTeam> result = new ArrayList<>();
        for (ExternalProjectXTeam externalprojectxteam : externalProjectTeamList) {
            if (externalprojectxteam.getTeamId().equals(teamId)) {
                result.add(externalprojectxteam);
            }
        }
        return result;
    }

    public ExternalProjectXTeam getExternalProjectTeamById(Integer configId) {
        return externalprojectxteamRepository.findById(configId).orElse(null);
    }

    public ExternalProjectXTeam postExternalProjectTeam(ExternalProjectXTeam externalprojectxteam) {
        return externalprojectxteamRepository.save(externalprojectxteam);
    }

    public ExternalProjectXTeam putExternalProjectTeam(Integer configId, ExternalProjectXTeam externalprojectxteam) {
        return externalprojectxteamRepository.findById(configId).map(externalProjectTeam -> {
            externalProjectTeam.setAgentConfig(externalprojectxteam.getAgentConfig());
            externalProjectTeam.setAgentId(externalprojectxteam.getAgentId());
            externalProjectTeam.setTeamId(externalprojectxteam.getTeamId());
            externalProjectTeam.setRepoId(externalprojectxteam.getRepoId());
            externalProjectTeam.setIsActive(externalprojectxteam.getIsActive());
            return externalprojectxteamRepository.save(externalProjectTeam);
        }).orElseGet(() -> externalprojectxteamRepository.save(externalprojectxteam));
    }

    public List<ExternalProjectXTeam> deleteExternalProjectTeamByAgentId(Integer agentId) {
        return deleteExternalProjectList(getExternalProjectTeamByAgentId(agentId));
    }

    public List<ExternalProjectXTeam> deleteExternalProjectTeamByTeamId(Integer teamId) {
        return deleteExternalProjectList(getExternalProjectTeamByTeamId(teamId));
    }

    public ExternalProjectXTeam deleteExternalProjectTeamById(Integer configId) {
        Optional<ExternalProjectXTeam> externalProjectTeam = externalprojectxteamRepository.findById(configId);
        if (!externalProjectTeam.isPresent()) {
            return null;
        }
        externalprojectxteamRepository.deleteById(configId);
        return externalProjectTeam.get();
    }

    private List<ExternalProjectXTeam> deleteExternalProjectList(List<ExternalProjectXTeam> externalProjectList) {
        if (externalProjectList == null || externalProjectList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ExternalProjectXTeam> deletedExternalProjectsList = new ArrayList<>(externalProjectList.size());
        for (ExternalProjectXTeam externalprojectxteam : externalProjectList) {
            ExternalProjectXTeam deletedExternalProjectTeam = deleteExternalProjectTeamById(externalprojectxteam.getConfigId());
            if (deletedExternalProjectTeam != null) {
                deletedExternalProjectsList.add(deletedExternalProjectTeam);
            }
        }
        return deletedExternalProjectsList;
    }
}
