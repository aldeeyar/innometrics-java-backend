package com.innopolis.innometrics.restapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "collector_version")
@Data
public class CollectorVersion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;

    @Column
    private String osVersion;

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
}
