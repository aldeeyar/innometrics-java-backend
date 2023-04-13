package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgentsProjectListResponse {
    private List<AgentsProjectDTO> agentsProjectList;

    public void add(AgentsProjectDTO agentsProjectDTO) {
        this.agentsProjectList.add(agentsProjectDTO);
    }
}
