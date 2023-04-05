package com.innopolis.innometrics.activitiescollector.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "processmeasurements")
@Data
public class ProcessMeasurement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Processid")
    private Process process;
    @Column(name = "measurementtypeid")
    private Integer measurementTypeId;
    @Column
    private String value;
    @Column(name = "alternativelabel")
    private String alternativeLabel;
    @Column(name = "captureddate")
    private Date capturedDate;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationdate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
}
