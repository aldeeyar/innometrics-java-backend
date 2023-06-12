package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoriesTimeReportResponse {
    private List<CategoriesReport> reports;
}
