package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.entity.ReposXProject;
import com.innopolis.innometrics.agentsgateway.repository.ReposXProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReposXProjectService {
    private final ReposXProjectRepository reposxprojectRepository;

    public List<ReposXProject> getReposProjectList() {
        return reposxprojectRepository.findAll();
    }

    public List<ReposXProject> getReposProjectByAgentId(Integer agentId) {
        List<ReposXProject> reposProjectList = reposxprojectRepository.findAll();
        List<ReposXProject> result = new ArrayList<>();
        for (ReposXProject reposxproject : reposProjectList) {
            if (reposxproject.getAgentId() != null && reposxproject.getAgentId().equals(agentId)) {
                result.add(reposxproject);
            }
        }
        return result;
    }

    public List<ReposXProject> getReposProjectByProjectId(Integer projectId) {
        return reposxprojectRepository.findByProjectId(projectId);
    }

    public ReposXProject getReposProjectById(Integer configId) {
        return reposxprojectRepository.findById(configId).orElse(null);
    }

    public ReposXProject postReposProject(ReposXProject reposxproject) {
        return reposxprojectRepository.save(reposxproject);
    }

    public ReposXProject putReposProject(Integer configId, ReposXProject reposxproject) {
        return reposxprojectRepository.findById(configId).map(reposProject -> {
            reposProject.setAgentId(reposxproject.getAgentId());
            reposProject.setProjectId(reposxproject.getProjectId());
            reposProject.setRepoId(reposxproject.getRepoId());
            reposProject.setIsActive(reposxproject.getIsActive());
            return reposxprojectRepository.save(reposProject);
        }).orElseGet(() -> reposxprojectRepository.save(reposxproject));
    }

    public List<ReposXProject> deleteReposProjectByAgentId(Integer agentId) {
        return deleteReposProjectList(getReposProjectByAgentId(agentId));
    }

    public List<ReposXProject> deleteReposProjectByProjectId(Integer projectId) {
        return deleteReposProjectList(getReposProjectByProjectId(projectId));
    }

    public ReposXProject deleteReposProjectById(Integer configId) {
        Optional<ReposXProject> reposProject = reposxprojectRepository.findById(configId);
        if (!reposProject.isPresent()) {
            return null;
        }
        reposxprojectRepository.deleteById(configId);
        return reposProject.get();
    }

    private List<ReposXProject> deleteReposProjectList(List<ReposXProject> reposProjectList) {
        if (reposProjectList == null || reposProjectList.isEmpty()) {
            return Collections.emptyList();
        }
        List<ReposXProject> deletedReposProjectsList = new ArrayList<>(reposProjectList.size());
        for (ReposXProject reposxproject : reposProjectList) {
            ReposXProject deletedReposProject = this.deleteReposProjectById(reposxproject.getConfigId());
            if (deletedReposProject != null) {
                deletedReposProjectsList.add(deletedReposProject);
            }
        }
        return deletedReposProjectsList;
    }
}
