package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.ActivitiesReportByUserResponse;
import com.innopolis.innometrics.restapi.dto.CumulativeReportResponse;
import com.innopolis.innometrics.restapi.dto.CurrentActivityReport;
import com.innopolis.innometrics.restapi.dto.TimeReportResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.innopolis.innometrics.restapi.constants.RequestConstants.DATE_PATTERN;

@Service
@RequiredArgsConstructor
public class ReportService {
    private static final String BASE_URL = "http://INNOMETRICS-COLLECTOR-SERVER/V1/Reports";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "getReportActivities", fallbackMethod = "getReportActivitiesFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public ActivitiesReportByUserResponse getReportActivities(String projectID, String email, Date minDate, Date maxDate) {
        String uri = BASE_URL + "/activitiesReport";
        Format formatter = new SimpleDateFormat(DATE_PATTERN.getValue());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("projectid", projectID)
                .queryParam(RequestConstants.EMAIL.name(), email)
                .queryParam(RequestConstants.MIN_DATE.getValue(), minDate != null ? formatter.format(minDate) : null)
                .queryParam(RequestConstants.MAX_DATE.getValue(), maxDate != null ? formatter.format(maxDate) : null);
        ResponseEntity<ActivitiesReportByUserResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, null, ActivitiesReportByUserResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getTimeReport", fallbackMethod = "getTimeReportFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TimeReportResponse getTimeReport(String projectID, String email, Date minDate, Date maxDate) {
        String uri = BASE_URL + "/timeReport";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Format formatter = new SimpleDateFormat(DATE_PATTERN.getValue());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("projectid", projectID == null ? "" : projectID)
                .queryParam(RequestConstants.EMAIL.name(), email == null ? "" : email)
                .queryParam(RequestConstants.MIN_DATE.getValue(), minDate != null ? formatter.format(minDate) : null)
                .queryParam(RequestConstants.MAX_DATE.getValue(), maxDate != null ? formatter.format(maxDate) : null);
        ResponseEntity<TimeReportResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, TimeReportResponse.class);
        return response.getBody();
    }


    @HystrixCommand(commandKey = "getCumulativeReport", fallbackMethod = "getCumulativeReportFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CumulativeReportResponse getCumulativeReport(String email, Date minDate, Date maxDate) {
        String uri = BASE_URL + "/cumulativeReport";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Format formatter = new SimpleDateFormat(DATE_PATTERN.getValue());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(RequestConstants.EMAIL.getValue(), email)
                .queryParam(RequestConstants.MIN_DATE.getValue(), minDate != null ? formatter.format(minDate) : null)
                .queryParam(RequestConstants.MAX_DATE.getValue(), maxDate != null ? formatter.format(maxDate) : null);
        ResponseEntity<CumulativeReportResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CumulativeReportResponse.class);
        return response.getBody();
    }


    @HystrixCommand(commandKey = "getExamReport", fallbackMethod = "getExamReportFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CurrentActivityReport getExamReport(String email) {
        String uri = BASE_URL + "/examReport";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(RequestConstants.EMAIL.getValue(), email);
        ResponseEntity<CurrentActivityReport> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CurrentActivityReport.class);
        return response.getBody();
    }
}
