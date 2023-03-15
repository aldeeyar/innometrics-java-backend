package com.innopolis.innometrics.restapi.dto;

import com.innopolis.innometrics.restapi.entity.Activity;
import lombok.Data;

import java.util.List;

@Data
public class ActivitiesReportResponse {
    private List<Activity> report;
}
