package com.innopolis.innometrics.activitiescollector.dto;

import java.util.Date;

public interface IProcessReportByUserAndDay {
    Integer getProcessID();

    String getEmail();

    String getExecutableName();

    String getPid();

    Date getCollectedTime();

    String getOsVersion();

    String getIpAddress();

    String getMacAddress();

    Date getCreationdate();

    String getCreatedBy();

    Date getLastupdate();

}
