package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MeasurementTypeRequest implements Serializable {
    private String label;
    private String description;
    private Float weight;
    private Float scale;
    private String operation;
}
