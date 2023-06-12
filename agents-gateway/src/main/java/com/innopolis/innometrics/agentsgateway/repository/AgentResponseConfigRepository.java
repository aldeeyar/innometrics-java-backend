package com.innopolis.innometrics.agentsgateway.repository;

import com.innopolis.innometrics.agentsgateway.entity.AgentResponseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentResponseConfigRepository extends JpaRepository<AgentResponseConfig, Integer> {
}
