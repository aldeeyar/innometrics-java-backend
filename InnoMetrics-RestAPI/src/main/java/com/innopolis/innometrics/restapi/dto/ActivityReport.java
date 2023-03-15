package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    private String pid;
    private String osVersion;
    private String ipAddress;
    private String macAddress;
}
