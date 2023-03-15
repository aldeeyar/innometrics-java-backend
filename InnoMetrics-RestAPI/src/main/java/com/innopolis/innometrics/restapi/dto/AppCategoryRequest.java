package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class AppCategoryRequest implements Serializable {
    private Integer appId;
    private String appName;
    private Integer categoryId;
    private String isActive;
}
