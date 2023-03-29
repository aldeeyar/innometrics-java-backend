package com.innopolis.innometrics.authserver.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeammembersListRequest {
    private List<TeammembersRequest> teammembersRequestList;

    public TeammembersListRequest() {
        teammembersRequestList = new ArrayList<>();
    }

    public List<TeammembersRequest> getTeammembersRequestList() {
        return teammembersRequestList;
    }

    public void addTeammembersRequest(TeammembersRequest teammembersRequest){
        this.teammembersRequestList.add(teammembersRequest);
    }
}
