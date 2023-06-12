package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.AddProcessReportRequest;
import com.innopolis.innometrics.restapi.dto.DeleteRequest;
import com.innopolis.innometrics.restapi.dto.ProcessDayReportResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProcessService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-COLLECTOR-SERVER/V1/process/";
    private final RestTemplate restTemplate;
    private final CommonMethodsService commonMethodsService;

    @HystrixCommand(commandKey = "createProcessReport", fallbackMethod = "createProcessReportFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean createProcessReport(AddProcessReportRequest report, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<AddProcessReportRequest> entity = new HttpEntity<>(report, headers);
        ResponseEntity<Object> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, Object.class);
        HttpStatus status = response.getStatusCode();
        return status == HttpStatus.OK;
    }

    @HystrixCommand(commandKey = "getProcessReportByEmail", fallbackMethod = "getProcessReportByEmailFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public ProcessDayReportResponse getProcessReportByEmail(Date reportDate, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("reportDate", reportDate != null ? formatter.format(reportDate) : null);
        HttpEntity<ProcessDayReportResponse> entity = new HttpEntity<>(headers);
        ResponseEntity<ProcessDayReportResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, ProcessDayReportResponse.class);
        return response.getBody();
    }

    @HystrixCommand( commandKey = "deleteProcessesWithIds", fallbackMethod = "deleteProcessesWithIdsFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean deleteProcessesWithIds(DeleteRequest request, String token) {
        return commonMethodsService.deleteWithIds(request, token, restTemplate, BASE_URL, LOG);
    }

}
