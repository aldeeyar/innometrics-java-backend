package com.innopolis.innometrics.restapi.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RoleResponse implements Serializable {
    private String name;
    private String description;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
    private PageListResponse pages;
}
