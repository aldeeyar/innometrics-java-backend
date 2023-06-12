package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

@Data
public class ActivitiesReportByUser implements IActivitiesReportByUser {
    private String email;
    private String executableName;
    private String timeUsed;
    private String activityDay;
    private String dateToSort;
}
