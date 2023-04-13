package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RepoDataPerProjectResponse implements Serializable {
    private List<RepoResponseDTO> repos;

    public RepoDataPerProjectResponse() {
        repos = new ArrayList<>();
    }
}
