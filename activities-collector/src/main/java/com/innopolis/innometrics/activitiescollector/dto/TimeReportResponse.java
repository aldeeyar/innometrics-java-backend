package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.util.List;

@Data
public class TimeReportResponse {
    private List<TimeReportByUser> report;
}
