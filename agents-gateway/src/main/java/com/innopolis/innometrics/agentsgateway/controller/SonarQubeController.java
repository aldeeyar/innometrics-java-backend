package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.MetricsResponse;
import com.innopolis.innometrics.agentsgateway.dto.ProjectListResponse;
import com.innopolis.innometrics.agentsgateway.service.SonarQubeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("sonarqube")
@AllArgsConstructor
public class SonarQubeController {
    private final SonarQubeService sonarQubeService;

    @GetMapping("projects")
    public ProjectListResponse getProjectList(){
        return sonarQubeService.getProjectList();
    }


    @GetMapping("metrics")
    public MetricsResponse getMetrics(){
        return sonarQubeService.getMetrics();
    }

    @GetMapping("metric/history")
    public ResponseEntity<Object> getHistoryForMetric(@RequestParam String innoProjectId,
                                                      @RequestParam String sonarProjectId) {
        Object o = sonarQubeService.getHistoryOfMetrics(sonarProjectId);
        if (o != null)
            return new ResponseEntity<>(o, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
