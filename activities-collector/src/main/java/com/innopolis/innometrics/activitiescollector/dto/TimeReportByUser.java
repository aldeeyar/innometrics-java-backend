package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

@Data
public class TimeReportByUser implements ITimeReportByUser {
    private String email;
    private String timeUsed;
    private String activityDay;
    private String dateToSort;
}
