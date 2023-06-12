package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

@Data
public class CumulativeActivityReport {
    private String email;
    private String usedTime;
    private String dailySum;
    private String monthlySum;
    private String yearlySum;
    private String capturedDate;
    private String executableName;
}
