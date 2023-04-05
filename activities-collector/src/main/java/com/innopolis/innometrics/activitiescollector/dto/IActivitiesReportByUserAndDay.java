package com.innopolis.innometrics.activitiescollector.dto;

import java.util.Date;

public interface IActivitiesReportByUserAndDay {
    Integer getActivityID();

    String getActivityType();

    Boolean getIdleActivity();

    String getEmail();

    Date getStartTime();

    Date getEndTime();

    String getExecutableName();

    String getBrowserUrl();

    String getBrowserTitle();

    String getPid();

    String getOsVersion();

    String getIpAddress();

    String getMacAddress();

    String getValue();

    Date getCreationdate();

    String getCreatedBy();

    Date getLastupdate();
}
