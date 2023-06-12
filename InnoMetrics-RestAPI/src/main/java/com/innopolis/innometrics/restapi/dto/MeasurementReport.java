package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MeasurementReport implements Serializable {
    private String measurementTypeId;
    private String value;
    private String alternativeLabel;
    private Date capturedDate;

}
