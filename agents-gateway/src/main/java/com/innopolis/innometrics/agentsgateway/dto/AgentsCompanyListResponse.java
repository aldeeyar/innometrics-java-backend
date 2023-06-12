package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgentsCompanyListResponse {
    private List<AgentsCompanyDTO> agentsCompanyList;

    public void add(AgentsCompanyDTO agentsCompanyDTO) {
        this.agentsCompanyList.add(agentsCompanyDTO);
    }
}
