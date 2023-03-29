package com.innopolis.innometrics.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CompanyRequest {
    private Integer companyId;
    private String companyName;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
}
