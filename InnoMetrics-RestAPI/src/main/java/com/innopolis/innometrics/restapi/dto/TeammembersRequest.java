package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TeammembersRequest {
    private Integer memberId;
    private Integer teamId;
    private String email;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
}
