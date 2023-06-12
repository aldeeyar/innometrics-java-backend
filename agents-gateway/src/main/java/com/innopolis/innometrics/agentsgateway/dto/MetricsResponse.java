package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.HashMap;

@Data
public class MetricsResponse {
    HashMap<String, String> metricList;

    public MetricsResponse() {
        metricList = new HashMap<>();
    }
}
