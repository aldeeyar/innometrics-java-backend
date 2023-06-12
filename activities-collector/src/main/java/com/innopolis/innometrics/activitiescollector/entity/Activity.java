package com.innopolis.innometrics.activitiescollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table
@Data
public class Activity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "activityid")
    private Integer activityId;
    @Column(name = "activitytype")
    private String activityType;
    @Column(name = "idle_activity")
    private Boolean idleActivity;
    @Column
    private String email;
    @Column(name = "start_time")
    private Date startTime;
    @Column(name = "end_time")
    private Date endTime;
    @Column(name = "executable_name")
    private String executableName;
    @Column(name = "browser_url")
    private String browserUrl;
    @Column(name = "browser_title")
    private String browserTitle;
    @Column
    private String pid;
    @Column(name = "osversion")
    private String osVersion;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "mac_address")
    private String macAddress;
    @Column
    private String value;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ActivityID")
    private Set<Measurement> measurements = new HashSet<>();
}
