package com.innopolis.innometrics.restapi.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryRequest implements Serializable {
    private Integer categoryId;
    private String categoryName;
    private String isActive;
}
