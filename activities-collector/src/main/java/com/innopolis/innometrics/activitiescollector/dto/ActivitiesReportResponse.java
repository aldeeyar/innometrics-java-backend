package com.innopolis.innometrics.activitiescollector.dto;

import com.innopolis.innometrics.activitiescollector.entity.Activity;
import lombok.Data;

import java.util.List;

@Data
public class ActivitiesReportResponse {
    private List<Activity> report;
}
