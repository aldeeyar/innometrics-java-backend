package com.innopolis.innometrics.agentsgateway.repository;

import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentConfigMethodsRepository extends JpaRepository<AgentConfigMethods, Integer> {
    List<AgentConfigMethods> findByAgentId(Integer agentId);

    AgentConfigMethods findByAgentIdAndOperation(Integer agentId, String operation);
}
