package com.innopolis.innometrics.activitiescollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "process")
@Data
public class Process implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "processid")
    private Integer processId;
    @Column
    private String email;
    @Column
    private Integer projectid;
    @Column(name = "executable_name")
    private String executableName;
    @Column
    private String pid;
    @Column(name = "osversion")
    private String osVersion;
    @Column(name = "collectedtime")
    private Date collectedTime;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "mac_address")
    private String macAddress;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationdate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ProcessID")
    private List<ProcessMeasurement> processMeasurements;
}
