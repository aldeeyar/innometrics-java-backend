package com.innopolis.innometrics.authserver.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class RoleResponse implements Serializable {
    private String name;
    private String description;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastUpdate;
    private String updateBy;
    private PageListResponse pages;

    @PreUpdate
    public void preUpdate() {
        this.lastUpdate = new Date();
    }
}
