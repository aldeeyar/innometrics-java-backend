package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.util.List;

@Data
public class BugTrackingListRequest {
    private List<BugTrackingRequest> bugTrackingRequests;

    public void addBugTrackingRequest(BugTrackingRequest bugTrackingRequest){
        this.bugTrackingRequests.add(bugTrackingRequest);
    }
}
