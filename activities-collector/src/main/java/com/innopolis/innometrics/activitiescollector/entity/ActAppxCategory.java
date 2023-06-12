package com.innopolis.innometrics.activitiescollector.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cl_apps_categories", schema = "innometricsconfig")
public class ActAppxCategory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer appid;
    @Column(name = "catid")
    private Integer catId;
    @Column(name = "appname")
    private String appName;
    @Column(name = "appdescription")
    private String appDescription;
    @Column(name = "executablefile")
    private String executableFile;
    @Column(name = "isactive")
    private String isActive;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationdate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
}
