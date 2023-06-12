package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TimeReportByUser implements Serializable {
    private String email;
    private String timeUsed;
    private String activityDay;
}
