package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.DTO.*;
import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.service.ActivityService;
import com.innopolis.innometrics.restapi.service.CategoryService;
import com.innopolis.innometrics.restapi.service.ProcessService;
import com.innopolis.innometrics.restapi.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/V1", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor
public class DataCollectorsAPI {

    @Autowired
    private JwtToken jwtTokenUtil;
    private final ActivityService activityService;
    private final ProcessService processService;
    private final ReportService reportService;
    private final CategoryService categoryService;


    @GetMapping("/activity")
    public ResponseEntity<ActivitiesReportResponse> getActivities(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date reportDate,
                                                                  @RequestHeader String token) {
        ActivitiesReportResponse myReport = activityService.getActivitiesByEmail(reportDate, token);
        return ResponseEntity.ok(myReport);
    }

    @PostMapping("/activity")
    public ResponseEntity<Void> addReport(@RequestBody Report report,
                                          UriComponentsBuilder ucBuilder,
                                          @RequestHeader String token) {

        String username = jwtTokenUtil.getUsernameFromToken(token);
        log.info("A request received from " + username + ", with " + report.getActivities().size() + " activities");
        if (report.getActivities().size() > 1000 || report.getActivities().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }
        boolean result = activityService.CreateActivity(report, token);
        if (result) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping("/activity")
    public ResponseEntity<Void> deleteActivitiesWithIds(@RequestBody DeleteRequest request,
                                                        @RequestHeader String token) {
        if (activityService.deleteActivitiesWithIds(request, token)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/process")
    public ResponseEntity<ProcessDayReportResponse> getProcesses(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date reportDate,
                                                                 @RequestHeader String token) {
        ProcessDayReportResponse myReport = processService.getProcessReportByEmail(reportDate, token);
        return ResponseEntity.ok(myReport);
    }

    //add process report
    @PostMapping("/process")
    public ResponseEntity<Void> addProcessReport(@RequestBody AddProcessReportRequest report,
                                                 UriComponentsBuilder ucBuilder,
                                                 @RequestHeader String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        log.info("A request received from " + username + ", with " + report.getProcessesReport().size() + " process");
        if (report.getProcessesReport().size() > 1000 || report.getProcessesReport().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }
        boolean result = processService.CreateProcessReport(report, token);
        if (result) return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping("/process")
    public ResponseEntity<Void> deleteProcessesWithIds(@RequestBody DeleteRequest request,
                                                       @RequestHeader String token) {
        if (processService.deleteProcessesWithIds(request, token)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/Reports/activitiesReport")
    public ResponseEntity<ActivitiesReportByUserResponse> getReportActivities(@RequestParam(required = false) String projectID,
                                                                              @RequestParam(required = false) String email,
                                                                              @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                                              @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        ActivitiesReportByUserResponse myReport = reportService.getReportActivities(projectID, email, minDate, maxDate);
        return ResponseEntity.ok(myReport);
    }

    @GetMapping("/Reports/timeReport")
    public ResponseEntity<TimeReportResponse> getTimeReport(@RequestParam(required = false) String projectID,
                                                            @RequestParam(required = false) String email,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        TimeReportResponse myReport = reportService.getTimeReport(projectID, email, minDate, maxDate);
        return ResponseEntity.ok(myReport);
    }

    @GetMapping("/Reports/cumulativeReport")
    public ResponseEntity<CumulativeReportResponse> getCumulativeReport(@RequestParam(required = false) String email,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        CumulativeReportResponse myReport = reportService.getCumulativeReport(email, minDate, maxDate);
        return ResponseEntity.ok(myReport);
    }

    @GetMapping("/Reports/categorytimeReport")
    public ResponseEntity<CategoriesTimeReportResponse> getCatTimeReport(@RequestParam(required = false) String projectID,
                                                                         @RequestParam(required = false) String email,
                                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                                         @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        CategoriesTimeReportResponse myReport = categoryService.getTimeReport(projectID, email, minDate, maxDate);
        return ResponseEntity.ok(myReport);
    }

    @GetMapping("/examReport")
    public ResponseEntity<CurrentActivityReport> getExamReport(@RequestParam(required = false) String email) {
        CurrentActivityReport myReport = reportService.getExamReport(email);
        return ResponseEntity.ok(myReport);
    }
}
