package com.innopolis.innometrics.agentsgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
public class ExternalProjectTeamDTO implements Serializable {
    private Integer configid;
    private Integer agentid;
    private Integer teamid;
    private String repoid;
    private String isactive;
    private Date creationdate;
    private String createdby;
    private Date lastupdate;
    private String updateby;
}
