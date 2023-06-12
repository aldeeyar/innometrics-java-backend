package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProcessReport implements Serializable {
    private String processName;
    private String userID;
    private String ipAddress;
    private String macAddress;
    private Date collectedTime;
    private String pid;
    private String osVersion;
    private List<MeasurementReport> measurementReportList;
}
