package com.innopolis.innometrics.activitiescollector.controller;


import com.innopolis.innometrics.activitiescollector.config.JwtToken;
import com.innopolis.innometrics.activitiescollector.dto.ActivitiesReportResponse;
import com.innopolis.innometrics.activitiescollector.dto.ActivityReport;
import com.innopolis.innometrics.activitiescollector.dto.DeleteRequest;
import com.innopolis.innometrics.activitiescollector.dto.Report;
import com.innopolis.innometrics.activitiescollector.service.ActivityService;
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
@RequestMapping(value = "/V1/activity", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ActivitiesCollector {
    private static final Logger LOG = LogManager.getLogger();
    private final ActivityService activityService;
    private final JwtToken jwtTokenUtil;

    @PostMapping("/")
    public ResponseEntity<Void> addReport(@RequestBody Report report,
                                          UriComponentsBuilder ucBuilder,
                                          @RequestHeader String token) {
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Date creationDate = new Date();
        LOG.info("a request received from {} , with {}", username, report.getActivities().size());
        if (report.getActivities().size() > 1000) {
            LOG.info("");
            return new ResponseEntity<>(HttpStatus.PAYLOAD_TOO_LARGE);
        }
        for (ActivityReport activity : report.getActivities()) {
            activityService.createActivity(activity, username, creationDate);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Integer activityId,
                                               @RequestHeader String token) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        if (activityService.deleteActivity(activityId, userName)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/")
    @ResponseBody
    public ActivitiesReportResponse getActivities(@RequestHeader String token,
                                                  @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") Date reportDate,
                                                  UriComponentsBuilder uriBuilder,
                                                  HttpServletResponse response) {
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        return activityService.getActivitiesByEmailAndDay(userName, reportDate);
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteActivitiesWithIds(@RequestBody DeleteRequest request,
                                                        @RequestHeader String token) {
        if (activityService.deleteActivitiesWithIds(request.getIds())) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
