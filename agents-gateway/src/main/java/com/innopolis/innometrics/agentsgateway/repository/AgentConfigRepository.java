package com.innopolis.innometrics.agentsgateway.repository;

import com.innopolis.innometrics.agentsgateway.dto.IAgentStatus;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentConfigRepository extends JpaRepository<AgentConfig, Integer> {
    AgentConfig findByAgentName(String agentName);

    AgentConfig findByAgentId(Integer agentId);


    @Modifying
    @Query(value = "select distinct agent.agentid,\n" +
            "       agent.agentname,\n" +
            "       agent.description,\n" +
            "       case when project.configid is not null then 'Y' else 'N' end isconnected\n" +
            "from innometricsagents.agentconfig agent\n" +
            "left outer join innometricsagents.agents_x_project project\n" +
            "  on agent.agentid = project.agentid\n" +
            "  and cast(project.projectid  as text) = cast(:ProjectID as text);", nativeQuery = true)
    List<IAgentStatus> getAgentList(@Param("ProjectID") Integer projectID);
}
