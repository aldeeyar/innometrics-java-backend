package com.innopolis.innometrics.agentsgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class AgentsCompanyDTO implements Serializable {
    private Integer configId;
    private Integer agentId;
    private Integer companyId;
    private String key;
    private String token;
    private String isActive;
    private Date creationdate;
    private String createdBy;
    private Date lastupdate;
    private String updateBy;
}
