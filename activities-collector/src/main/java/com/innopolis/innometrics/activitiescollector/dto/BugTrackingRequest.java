package com.innopolis.innometrics.activitiescollector.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BugTrackingRequest implements Serializable {
    private Integer bugId;
    private String username;
    private String title;
    private String classOfBug;
    private Integer line;
    private String comment;
    private String trace;
    private String os;
    private String dataCollectorVersion;
    private Date creationdate;
    private Date lastupdate;
    private boolean status;
}
