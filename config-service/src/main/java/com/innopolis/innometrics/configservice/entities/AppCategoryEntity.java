package com.innopolis.innometrics.configservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "cl_apps_categories")
public class AppCategoryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appid", updatable = false)
    private Integer appId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "catid")
    private CategoryEntity categoryEntity;
    @Column(name = "appname")
    private String appName;
    @Column(name = "appdescription")
    private String appDescription;
    @Column(name = "executablefile")
    private String executableFile;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
}
