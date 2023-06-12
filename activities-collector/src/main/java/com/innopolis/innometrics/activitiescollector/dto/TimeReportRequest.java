package com.innopolis.innometrics.activitiescollector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class TimeReportRequest implements Serializable {
    private String projectID;
    private String email;
    private Date minDate;
    private Date maxDate;
}
