package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BugReportRequest implements Serializable {
    private String username;
    private String title;
    private String classOfBug;
    private Integer line;
    private String trace;
    private String os;
    private String dataCollectorVersion;
}
