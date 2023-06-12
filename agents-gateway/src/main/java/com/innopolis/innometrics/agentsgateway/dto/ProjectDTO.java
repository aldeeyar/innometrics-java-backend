package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectDTO implements Serializable {
    private String projectId;
    private String projectName;
    private String reference;
    private String isConnected;
}
