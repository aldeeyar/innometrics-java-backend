package com.innopolis.innometrics.agentsgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailsConfigDTO implements Serializable {
    private Integer configDetId;
    private Integer methodId;
    private String paramName;
    private String paramType;
    private String requestParam;
    private String requestType;
    private String isActive;
    private String defaultValue;
    private Date creationDate;
    private String createdBy;
    private Date lastupdate;
    private String updateBy;
}
