package com.innopolis.innometrics.agentsgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse implements Serializable {
    private Integer agentId;
    private String agentName;
    private String description;
    private String isConnected;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
    private String sourceType;
    private String dbSchemaSource;
    private String repoIdField;
    private String authUri;
    private String authenticationMethod;
    private String accessTokenEndpoint;
    private String authorizationBaseUrl;
    private String requestTokenEndpoint;
    private String apiKey;
    private String apiSecret;

    public AgentResponse(Integer agentId, String agentName, String description, String isConnected) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.description = description;
        this.isConnected = isConnected;
    }
}
