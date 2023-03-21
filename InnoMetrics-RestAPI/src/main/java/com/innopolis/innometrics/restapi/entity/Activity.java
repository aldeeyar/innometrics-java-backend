package com.innopolis.innometrics.restapi.entity;

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
    @Column(updatable = false)
    private Integer activityId;

    @Column
    private String activityType;

    @Column
    private Boolean idleActivity;

    @Column
    private String email;

    @Column
    private Date startTime;

    @Column
    private Date endTime;

    @Column
    private String executableName;

    @Column
    private String browserUrl;

    @Column
    private String browserTitle;

    @Column
    private String ipAddress;

    @Column
    private String macAddress;

    @Column
    private String value;

    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;

    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;

    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;

    @Column(name = "updateby", insertable = false)
    private String updateBy;

    @OneToMany
    @JoinColumn(name = "ActivityID")
    private Set<Measurement> measurements = new HashSet<>();
}
