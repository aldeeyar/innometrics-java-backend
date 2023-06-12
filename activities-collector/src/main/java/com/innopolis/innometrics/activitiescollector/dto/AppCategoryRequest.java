package com.innopolis.innometrics.activitiescollector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppCategoryRequest implements Serializable {
    private Integer appId;
    private String appName;
    private Integer catId;
    private String isActive;
}
