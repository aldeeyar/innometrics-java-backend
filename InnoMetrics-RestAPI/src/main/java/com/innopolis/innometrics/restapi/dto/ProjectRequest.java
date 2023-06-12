package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProjectRequest implements Serializable {
    private Integer projectID;
    private String name;
    private String isActive;
}
