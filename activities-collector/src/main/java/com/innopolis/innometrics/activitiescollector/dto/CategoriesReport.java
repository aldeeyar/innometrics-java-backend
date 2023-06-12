package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

@Data
public class CategoriesReport implements ICategoriesReport {
    private String catName;
    private String catDescription;
    private String timeUsed;
}
