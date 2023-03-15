package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ActivitiesReportByUserResponse implements Serializable {
    private List<ActivitiesReportByUser> report;
}
