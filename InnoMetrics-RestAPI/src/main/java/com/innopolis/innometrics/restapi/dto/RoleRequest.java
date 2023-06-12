package com.innopolis.innometrics.restapi.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class RoleRequest implements Serializable {
    private String name;
    private String description;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastupDate;
    private String updateBy;
    private List<PageRequest> pages;
}
