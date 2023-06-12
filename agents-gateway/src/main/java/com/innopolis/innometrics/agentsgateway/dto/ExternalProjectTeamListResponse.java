package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalProjectTeamListResponse {
    private List<ExternalProjectTeamDTO> externalProjectTeamList;

    public void add(ExternalProjectTeamDTO externalProjectTeamDTO) {
        this.externalProjectTeamList.add(externalProjectTeamDTO);
    }
}
