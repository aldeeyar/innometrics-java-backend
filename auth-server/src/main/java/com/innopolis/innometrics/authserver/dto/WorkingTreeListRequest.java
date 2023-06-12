package com.innopolis.innometrics.authserver.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkingTreeListRequest {
    private List<WorkingTreeRequest> workingTreeRequests;

    public WorkingTreeListRequest() {
        this.workingTreeRequests = new ArrayList<>();
    }

    public void addWorkingTreeRequest(WorkingTreeRequest workingTreeRequests) {
        this.workingTreeRequests.add(workingTreeRequests);
    }
}
