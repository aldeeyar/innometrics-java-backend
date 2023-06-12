package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

@Data
public class WorkingTreeRequest {
    private String email;
    private Integer memberId;
    private Integer teamId;
    private String teamName;
    private String teamDescription;
    private Integer companyId;
    private String companyName;
    private Integer projectID;
    private String projectname;
}
