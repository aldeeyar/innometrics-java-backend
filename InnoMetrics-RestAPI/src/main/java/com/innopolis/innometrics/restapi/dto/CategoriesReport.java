package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

@Data
public class CategoriesReport {
    private String categoryName;
    private String categoryDescription;
    private String timeUsed;
}
