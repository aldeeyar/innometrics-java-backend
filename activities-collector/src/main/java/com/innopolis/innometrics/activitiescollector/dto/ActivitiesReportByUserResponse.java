package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.util.List;

@Data
public class ActivitiesReportByUserResponse {
    private List<ActivitiesReportByUser> report;
}
