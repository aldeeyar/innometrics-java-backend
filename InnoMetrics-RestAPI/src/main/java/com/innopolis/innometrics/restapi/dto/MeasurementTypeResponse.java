package com.innopolis.innometrics.restapi.dto;

import com.innopolis.innometrics.restapi.entity.MeasurementType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MeasurementTypeResponse implements Serializable {
    private Integer measurementTypeId;
    private String label;
    private String description;
    private Float weight;
    private Float scale;
    private String operation;
    private String isActive;
    private Date creationDate;
    private String createdBy;
    private Date lastupdate;
    private String updateBy;

    public MeasurementTypeResponse(MeasurementType measurementType) {
        this.measurementTypeId = measurementType.getMeasurementTypeId();
        this.label = measurementType.getLabel();
        this.description = measurementType.getDescription();
        this.weight = measurementType.getWeight();
        this.scale = measurementType.getScale();
        this.operation = measurementType.getOperation();
        this.isActive = measurementType.getIsActive();
        this.creationDate = measurementType.getCreationDate();
        this.createdBy = measurementType.getCreatedBy();
        this.lastupdate = measurementType.getLastUpdate();
        this.updateBy = measurementType.getUpdateBy();
    }
}
