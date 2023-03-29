package com.innopolis.innometrics.authserver.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RoleRequest implements Serializable {
    private String name;
    private String description;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
    private List<PageResponse> pages;

    @PreUpdate
    public void preUpdate() {
        this.lastUpdate = new Date();
    }
}
