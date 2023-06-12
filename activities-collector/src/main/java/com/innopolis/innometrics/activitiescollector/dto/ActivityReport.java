package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ActivityReport implements Serializable {
    private Integer activityID;
    private String activityType;
    private Boolean idleActivity;
    private String userID;
    private Date startTime;
    private Date endTime;
    private String executableName;
    private String browserUrl;
    private String browserTitle;
    private String ipAddress;
    private String macAddress;
    private String pid;
    private String osVersion;
    private Set<MeasurementReport> measurements = new HashSet<>();
}
