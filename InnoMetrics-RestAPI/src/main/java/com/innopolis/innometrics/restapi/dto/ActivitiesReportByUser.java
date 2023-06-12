package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivitiesReportByUser implements Serializable {
    private static final long serialVersionUID = 3801383401971413246L;
    private String email;
    private String executableName;
    private String timeUsed;
    private String activityDay;
}
