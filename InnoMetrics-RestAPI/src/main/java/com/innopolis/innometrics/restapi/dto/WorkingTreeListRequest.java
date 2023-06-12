package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class WorkingTreeListRequest {
    private List<WorkingTreeRequest> workingTreeRequests;
}
