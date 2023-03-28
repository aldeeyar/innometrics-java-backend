package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class BugTrackingListRequest {
    private List<BugTrackingRequest> bugTrackingRequests;
}
