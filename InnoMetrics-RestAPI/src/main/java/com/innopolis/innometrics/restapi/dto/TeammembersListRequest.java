package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class TeammembersListRequest {
    private List<TeammembersRequest> teamMembersRequestList;
}
