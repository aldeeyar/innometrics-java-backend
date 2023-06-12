package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AgentListResponse {
    private List<AgentResponse> agentList;

    public AgentListResponse() {
        agentList = new ArrayList<>();
    }

    public void add(AgentResponse agentResponse) {
        agentList.add(agentResponse);
    }

    public boolean isEmpty() {
        return agentList.isEmpty();
    }
}
