package com.innopolis.innometrics.authserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TeamRequest {
    private Integer teamId;
    private String teamName;
    private Integer companyId;
    private Integer projectID;
    private String description;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
}
