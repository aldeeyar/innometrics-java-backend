package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RepoResponseDTO implements Serializable {
    private String agentName;
    private List<RepoEventDTO> events;
}
