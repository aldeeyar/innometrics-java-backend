package com.innopolis.innometrics.restapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@Data
public class Role implements Serializable {
    @Id
    @Column(updatable = false)
    private String name;
    @Column
    private String description;
    @Column
    private String isActive;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();
    @OneToMany(mappedBy = "role")
    private Set<Permission> permissions = new HashSet<>();
    @PreUpdate
    public void preUpdate(){
        this.lastUpdate = new Date();
    }

}
