package com.innopolis.innometrics.agentsgateway.repository;

import com.innopolis.innometrics.agentsgateway.entity.AgentsXProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentsXProjectRepository extends JpaRepository<AgentsXProject, Integer> {
    AgentsXProject findByAgentIdAndProjectId(Integer agentId, Integer projectId);
}
