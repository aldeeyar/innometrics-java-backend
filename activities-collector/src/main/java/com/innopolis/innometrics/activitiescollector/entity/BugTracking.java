package com.innopolis.innometrics.activitiescollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@Table(name = "bug_tracking")
public class BugTracking implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bugId;
    @Column(name = "username")
    private String userName;
    @Column
    private String title;
    @Column(name = "class")
    private String classOfBug;
    @Column
    private Integer line;
    @Column
    private String comment;
    @Column
    private String trace;
    @Column
    private String os;
    @Column
    private String dataCollectorVersion;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "lastupdate")
    private Date lastUpdate;
    @Column
    private boolean status;
}
