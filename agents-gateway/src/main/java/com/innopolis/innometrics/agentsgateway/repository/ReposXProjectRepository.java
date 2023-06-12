package com.innopolis.innometrics.agentsgateway.repository;

import com.innopolis.innometrics.agentsgateway.entity.ReposXProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReposXProjectRepository extends JpaRepository<ReposXProject, Integer> {
    List<ReposXProject> findByProjectId(Integer projectId);

    List<ReposXProject> findByRepoId(String repoId);
}
