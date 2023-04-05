package com.innopolis.innometrics.activitiescollector.controller;


import com.innopolis.innometrics.activitiescollector.config.JwtToken;
import com.innopolis.innometrics.activitiescollector.dto.AddProcessReportRequest;
import com.innopolis.innometrics.activitiescollector.dto.DeleteRequest;
import com.innopolis.innometrics.activitiescollector.dto.ProcessDayReportResponse;
import com.innopolis.innometrics.activitiescollector.dto.ProcessReport;
import com.innopolis.innometrics.activitiescollector.service.ProcessService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping(value = "/V1/process", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProcessCollector {
    private static final Logger LOG = LogManager.getLogger();
    private final ProcessService processService;
    private final JwtToken jwtTokenUtil;

    @PostMapping("/")
    public ResponseEntity<Void> addProcessReport(@RequestBody AddProcessReportRequest report,
                                                 UriComponentsBuilder ucBuilder,
                                                 @RequestHeader String token) {

        String userName = jwtTokenUtil.getUsernameFromToken(token);
        Date creationDate = new Date();
        LOG.info("A request received from {}, with {} process", userName, report.getProcessReports().size());
        if (report.getProcessReports().size() > 1000) {
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }
        for (ProcessReport activity : report.getProcessReports()) {
            processService.createProcessReport(activity, userName, creationDate);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ProcessDayReportResponse> getProcessReport(@RequestHeader String token,
                                                                     @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date reportDate,
                                                                     UriComponentsBuilder uriBuilder,
                                                                     HttpServletResponse response) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        ProcessDayReportResponse myReport = processService.getProcessesByEmailAndDay(userName, reportDate);
        return ResponseEntity.ok(myReport);
    }


    @DeleteMapping("/{processId}")
    public ResponseEntity<Void> deleteProcess(@PathVariable Integer processId,
                                              @RequestHeader String token) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        if (processService.deleteProcess(processId, userName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteProcessesWithIds(@RequestBody DeleteRequest request,
                                                       @RequestHeader String token) {
        if (processService.deleteProcessesWithIds(request.getIds())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
