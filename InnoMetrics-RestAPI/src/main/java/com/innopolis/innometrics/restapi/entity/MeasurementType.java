package com.innopolis.innometrics.restapi.entity;

import lombok.Data;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "measurementtypes")
@Data
public class MeasurementType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measurementtypeid", updatable = false)
    private Integer measurementTypeId;
    @Column
    private String label;
    @Column
    private String description;
    @Column
    private Float weight;
    @Column
    private Float scale;
    @Column
    private String operation;
    @Column(name = "isactive")
    private String isActive;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
