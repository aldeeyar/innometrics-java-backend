package com.innopolis.innometrics.authserver.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamListRequest {
    private List<TeamRequest> teamRequestList;

    public TeamListRequest() {
        teamRequestList = new ArrayList<>();
    }

    public void addTeamRequest(TeamRequest teamRequestList){
        this.teamRequestList.add(teamRequestList);
    }
}
