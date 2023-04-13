package com.innopolis.innometrics.agentsgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataConfigDTO implements Serializable {
    private Integer dataConfigId;
    private Integer agentId;
    private String schemaName;
    private String tableName;
    private String eventDateField;
    private String eventAuthorField;
    private String eventDescriptionField;
    private String isActive;
    private Date creationdate;
    private String createdBy;
    private Date lastupdate;
    private String updateBy;
    private String eventType;
}
