package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class ReposProjectListResponse {
    private List<ReposProjectDTO> reposProjectList;

    public void add(ReposProjectDTO reposProjectDTO) {
        this.reposProjectList.add(reposProjectDTO);
    }
}
