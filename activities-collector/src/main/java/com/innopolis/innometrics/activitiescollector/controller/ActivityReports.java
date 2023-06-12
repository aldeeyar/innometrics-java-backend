package com.innopolis.innometrics.activitiescollector.controller;

import com.innopolis.innometrics.activitiescollector.dto.*;
import com.innopolis.innometrics.activitiescollector.service.ActivityService;
import com.innopolis.innometrics.activitiescollector.service.ProcessService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping(value = "/V1/Reports", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ActivityReports {
    private final ActivityService activityService;
    private final ProcessService processService;

    @GetMapping("/activitiesReport")
    public ResponseEntity<ActivitiesReportByUserResponse> getReportActivitiesByUser(@RequestParam(required = false) String projectid,
                                                                                    @RequestParam(required = false) String email,
                                                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                                                    @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        projectid = Objects.equals(projectid, "") ? null : projectid;
        email = Objects.equals(email, "") ? null : email;
        ActivitiesReportByUserRequest request = new ActivitiesReportByUserRequest(projectid, email, minDate, maxDate);
        ActivitiesReportByUserResponse myReport = activityService.getActivitiesReportByUser(request);
        return ResponseEntity.ok(myReport);
    }

    @GetMapping("/timeReport")
    public ResponseEntity<TimeReportResponse> getTimeReport(@RequestParam(required = false, name = "projectid") String projectid,
                                                            @RequestParam(required = false, name = "email") String email,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        projectid = Objects.equals(projectid, "") ? null : projectid;
        email = Objects.equals(email, "") ? null : email;
        TimeReportRequest request = new TimeReportRequest(projectid, email, minDate, maxDate);
        TimeReportResponse myReport = activityService.getTimeReportByUser(request);
        return ResponseEntity.ok(myReport);
    }

    @GetMapping("/cumulativeReport")
    public ResponseEntity<CumulativeReportResponse> getCumulativeReport(@RequestParam(required = false) String email,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        CumulativeReportResponse myReport = activityService.getCumulativeReportByEmail(email, minDate, maxDate);
        return ResponseEntity.ok(myReport);
    }


    @GetMapping("/examReport")
    public ResponseEntity<CurrentActivityReport> getExamReport(@RequestParam(required = false) String email) {
        CurrentActivityReport myReport = processService.getCurrentActivityReport(email, new Date());
        return ResponseEntity.ok(myReport);
    }
}
