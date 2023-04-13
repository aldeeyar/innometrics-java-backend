package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.AgentsXProject;
import com.innopolis.innometrics.agentsgateway.repository.AgentsXProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgentsXProjectService {
    private final AgentsXProjectRepository agentsxprojectRepository;

    public List<AgentsXProject> getAgentsProjectList() {
        return agentsxprojectRepository.findAll();
    }

    public List<AgentsXProject> getAgentsProjectByAgentId(Integer agentId) {
        List<AgentsXProject> agentsProjectList = agentsxprojectRepository.findAll();
        List<AgentsXProject> result = new ArrayList<>();
        for (AgentsXProject agentsxproject : agentsProjectList) {
            if (agentsxproject.getAgentId() != null && agentsxproject.getAgentId().equals(agentId)) {
                result.add(agentsxproject);
            }
        }
        return result;
    }

    public List<AgentsXProject> getAgentsProjectByProjectId(Integer projectId) {
        List<AgentsXProject> agentsProjectList = agentsxprojectRepository.findAll();
        List<AgentsXProject> result = new ArrayList<>();
        for (AgentsXProject agentsxproject : agentsProjectList) {
            if (agentsxproject.getProjectId().equals(projectId)) {
                result.add(agentsxproject);
            }
        }
        return result;
    }

    public AgentsXProject getAgentsProjectByAgentIdAndProjectId(Integer agentId, Integer projectId) {
        return agentsxprojectRepository.findByAgentIdAndProjectId(agentId, projectId);
    }

    public AgentsXProject getAgentsProjectById(Integer configId) {
        return agentsxprojectRepository.findById(configId).orElse(null);
    }

    public AgentsXProject postAgentsProject(AgentsXProject agentsxproject) {
        return agentsxprojectRepository.save(agentsxproject);
    }

    public AgentsXProject putAgentsProject(Integer configId, AgentsXProject agentsxproject) {
        return agentsxprojectRepository.findById(configId).map(agentsProject -> {
            agentsProject.setAgentConfig(agentsxproject.getAgentConfig());
            agentsProject.setAgentId(agentsxproject.getAgentId());
            agentsProject.setProjectId(agentsxproject.getProjectId());
            agentsProject.setKey(agentsxproject.getKey());
            agentsProject.setToken(agentsxproject.getToken());
            agentsProject.setIsActive(agentsxproject.getIsActive());
            return agentsxprojectRepository.save(agentsProject);
        }).orElseGet(() -> agentsxprojectRepository.save(agentsxproject));
    }

    public List<AgentsXProject> deleteAgentsProjectByAgentId(Integer agentId) {
        return deleteAgentsProjectList(getAgentsProjectByAgentId(agentId));
    }

    public List<AgentsXProject> deleteAgentsProjectByProjectId(Integer projectId) {
        return deleteAgentsProjectList(getAgentsProjectByProjectId(projectId));
    }

    public AgentsXProject deleteAgentsProjectById(Integer configId) {
        Optional<AgentsXProject> agentsProject = agentsxprojectRepository.findById(configId);
        if (!agentsProject.isPresent()) {
            return null;
        }
        agentsxprojectRepository.deleteById(configId);
        return agentsProject.get();
    }

    private List<AgentsXProject> deleteAgentsProjectList(List<AgentsXProject> agentsProjectList) {
        if (agentsProjectList == null || agentsProjectList.isEmpty()) {
            return Collections.emptyList();
        }
        List<AgentsXProject> deletedAgentsProjectsList = new ArrayList<>(agentsProjectList.size());
        for (AgentsXProject agentsxproject : agentsProjectList) {
            AgentsXProject deletedAgentsProject = this.deleteAgentsProjectById(agentsxproject.getConfigId());
            if (deletedAgentsProject != null) {
                deletedAgentsProjectsList.add(deletedAgentsProject);
            }
        }
        return deletedAgentsProjectsList;
    }
}
