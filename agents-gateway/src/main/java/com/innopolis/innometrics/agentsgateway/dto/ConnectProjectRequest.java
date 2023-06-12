package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ConnectProjectRequest implements Serializable {
    private Integer agentId;
    private Integer projectID;
    private String repoReference;
}
