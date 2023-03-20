package com.innopolis.innometrics.restapi.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "measurements")
@Data
public class Measurement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activityid")
    private Activity activity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "measurementtypeid")
    private MeasurementType measurementType;
    @Column
    private String value;
    @Column
    private String alternativeLabel;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastupdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
}
