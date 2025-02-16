package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
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
    /**
     * 0 - not solved, 1 - solved
     */
    private boolean status;

}
