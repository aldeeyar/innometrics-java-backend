package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class MethodConfigDTO implements Serializable {
    private Integer methodId;
    private String description;
    private String operation;
    private Integer agentId;
    private String endpoint;
    private String isActive;
    private Date creationdate;
    private String createdBy;
    private Date lastupdate;
    private String updateBy;
    private String requestType;
    private List<ParamsConfigDTO> configParameters;

    public MethodConfigDTO(Integer methodId, String description, String operation, List<ParamsConfigDTO> configParameters) {
        this.methodId = methodId;
        this.description = description;
        this.operation = operation;
        this.configParameters = configParameters;
    }
}
