package com.innopolis.innometrics.authserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectListRequest implements Serializable {
    private List<ProjectRequest> projectList;

    public ProjectListRequest() {
        projectList = new ArrayList<>();
    }
}
