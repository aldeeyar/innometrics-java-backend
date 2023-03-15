package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TimeReportRequest implements Serializable {
    private String projectID;
    private String email;
    private Date minDate;
    private Date maxDate;
}
