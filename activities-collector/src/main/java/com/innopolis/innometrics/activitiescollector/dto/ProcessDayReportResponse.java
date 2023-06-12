package com.innopolis.innometrics.activitiescollector.dto;

import com.innopolis.innometrics.activitiescollector.entity.Process;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProcessDayReportResponse implements Serializable {
    private List<Process> processReports;
}
