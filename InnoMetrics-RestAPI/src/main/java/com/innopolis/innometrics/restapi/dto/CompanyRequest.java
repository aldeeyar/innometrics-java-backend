package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CompanyRequest {
    private Integer companyId;
    private String companyName;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
}
