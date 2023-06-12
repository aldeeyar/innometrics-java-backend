package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BugTrackingListRequest {
    private List<BugTrackingRequest> bugTrackingRequests;

    public void addBugTrackingRequest(BugTrackingRequest bugTrackingRequest){
        this.bugTrackingRequests.add(bugTrackingRequest);
    }
}
