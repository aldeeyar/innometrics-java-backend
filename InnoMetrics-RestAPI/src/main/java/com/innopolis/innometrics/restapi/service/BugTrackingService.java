package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.BugReportRequest;
import com.innopolis.innometrics.restapi.dto.BugTrackingListRequest;
import com.innopolis.innometrics.restapi.dto.BugTrackingRequest;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class BugTrackingService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-COLLECTOR-SERVER/V1/Bug";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "createBug", fallbackMethod = "createBugFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean createBug(BugReportRequest bug, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<BugReportRequest> entity = new HttpEntity<>(bug, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, Object.class);
            HttpStatus status = response.getStatusCode();
            return status == HttpStatus.OK;
        } catch (Exception e) {
            LOG.warn(e);
            return false;
        }
    }

    @HystrixCommand(commandKey = "updateBug", fallbackMethod = "updateBugFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean updateBug(BugTrackingRequest bug, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<BugTrackingRequest> entity = new HttpEntity<>(bug, headers);
        try {
            ResponseEntity<Object> response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, entity, Object.class);
            HttpStatus status = response.getStatusCode();
            return status == HttpStatus.OK;
        } catch (Exception e) {
            LOG.warn(e);
            return false;
        }
    }

    @HystrixCommand(commandKey = "findBugById", fallbackMethod = "findBugByIdFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public BugTrackingRequest findBugById(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<BugTrackingRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("bugId", id);
        ResponseEntity<BugTrackingRequest> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, BugTrackingRequest.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "findBugsByParameters", fallbackMethod = "findBugsByParametersFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public BugTrackingListRequest findBugsByParameters(Integer status, String creationDate1, String creationDate2, String token) {
        String uri = BASE_URL + "/all";
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<BugTrackingListRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("creationdateFrom", creationDate1)
                .queryParam("creationdateTo", creationDate2)
                .queryParam("status", status);
        ResponseEntity<BugTrackingListRequest> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, BugTrackingListRequest.class);
        return response.getBody();
    }

    @HystrixCommand( commandKey = "deleteBug", fallbackMethod = "deleteBugFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public void deleteBug(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<BugTrackingRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("bugId", id);
        restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class);
    }
}
