package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectListResponse {
    private Integer agentId;
    private List<ProjectDTO> projectList;

    public ProjectListResponse() {
        projectList = new ArrayList<>();
    }
}
