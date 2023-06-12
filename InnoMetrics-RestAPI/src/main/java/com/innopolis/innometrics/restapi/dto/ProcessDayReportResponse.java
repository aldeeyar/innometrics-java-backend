package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProcessDayReportResponse implements Serializable {
    private List<Process> processReports;
}
