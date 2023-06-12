package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CumulativeReportResponse implements Serializable {
    private List<CumulativeActivityReport> activityReports;
}
