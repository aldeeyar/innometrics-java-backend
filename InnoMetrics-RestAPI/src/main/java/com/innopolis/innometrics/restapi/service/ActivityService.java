package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.ActivitiesReportResponse;
import com.innopolis.innometrics.restapi.dto.DeleteRequest;
import com.innopolis.innometrics.restapi.dto.Report;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.innopolis.innometrics.restapi.constants.RequestConstants.DATE_PATTERN;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService {
    private static final String BASE_URL = "http://INNOMETRICS-COLLECTOR-SERVER/V1/activity/";
    private static final Logger LOG = LogManager.getLogger();
    private final RestTemplate restTemplate;
    private final CommonMethodsService commonMethodsService;

    @HystrixCommand(commandKey = "createActivity", fallbackMethod = "createActivityFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean createActivity(Report report, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<Report> entity = new HttpEntity<>(report, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, Object.class);
            HttpStatus status = response.getStatusCode();
            return status == HttpStatus.OK;
        } catch (Exception e) {
            LOG.warn(e);
            return false;
        }
    }

    @HystrixCommand(commandKey = "getActivitiesByEmail", fallbackMethod = "getActivitiesByEmailFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public ActivitiesReportResponse getActivitiesByEmail(Date reportDate, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        Format formatter = new SimpleDateFormat(DATE_PATTERN.getValue());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("reportDate", reportDate != null ? formatter.format(reportDate) : null);
        HttpEntity<ActivitiesReportResponse> entity = new HttpEntity<>(headers);
        ResponseEntity<ActivitiesReportResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, ActivitiesReportResponse.class);
        return response.getBody();
    }

    @HystrixCommand( commandKey = "deleteActivitiesWithIds", fallbackMethod = "deleteActivitiesWithIdsFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean deleteActivitiesWithIds(DeleteRequest request, String token) {
        return commonMethodsService.deleteWithIds(request, token, restTemplate, BASE_URL, LOG);
    }
}
