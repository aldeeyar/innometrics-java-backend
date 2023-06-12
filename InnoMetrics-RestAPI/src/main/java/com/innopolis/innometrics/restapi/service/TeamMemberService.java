package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.TeammembersListRequest;
import com.innopolis.innometrics.restapi.dto.TeammembersRequest;
import com.innopolis.innometrics.restapi.dto.WorkingTreeListRequest;
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
public class TeamMemberService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/Teammember";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "updateTeamMember", fallbackMethod = "updateTeamMemberFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeammembersRequest updateTeamMember(TeammembersRequest teammembersRequest, String token) {
        String uri = BASE_URL + "/" + teammembersRequest.getMemberId();
        return uploadTeamMember(teammembersRequest, token, uri, HttpMethod.PUT);
    }

    public TeammembersRequest createTeamMember(TeammembersRequest teammembersRequest, String token) {
        return uploadTeamMember(teammembersRequest, token, BASE_URL, HttpMethod.POST);
    }

    private TeammembersRequest uploadTeamMember(TeammembersRequest teammembersRequest, String token, String uri, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeammembersRequest> entity = new HttpEntity<>(teammembersRequest, headers);
        ResponseEntity<TeammembersRequest> response = restTemplate.exchange(uri, method, entity, TeammembersRequest.class);
        HttpStatus status = response.getStatusCode();
        if (status == HttpStatus.NO_CONTENT) return null;
        return response.getBody();
    }

    public boolean deleteTeamMember(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeammembersRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam(RequestConstants.ID.getValue(), id);
        try {
            restTemplate.exchange(builder.toUriString(), HttpMethod.DELETE, entity, Object.class);
            return true;
        } catch (Exception e) {
            LOG.error(e);
            return false;
        }
    }

    @HystrixCommand(commandKey = "getActiveTeamMembers", fallbackMethod = "getActiveTeamMembersFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeammembersListRequest getActiveTeamMembers(String token) {
        String uri = BASE_URL + "/active";
        return getTeammembers(token, uri);
    }

    @HystrixCommand(commandKey = "getAllTeammembers", fallbackMethod = "getAllTeammembersFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeammembersListRequest getAllTeammembers(String token) {
        String uri = BASE_URL + "/all";
        return getTeammembers(token, uri);
    }

    private TeammembersListRequest getTeammembers(String token, String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        ResponseEntity<TeammembersListRequest> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, TeammembersListRequest.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getTeammembersBy", fallbackMethod = "getTeammembersByFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeammembersListRequest getTeammembersBy(Integer memberId, Integer teamId, String email, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeammembersListRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("teamId", teamId).queryParam("memberId", memberId).queryParam("email", email);
        ResponseEntity<TeammembersListRequest> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, TeammembersListRequest.class);
        return responseEntity.getBody();
    }

    @HystrixCommand(commandKey = "getWorkingTree", fallbackMethod = "getWorkingTreeFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public WorkingTreeListRequest getWorkingTree(String email, String token) {
        String uri = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/WorkingTree";
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<WorkingTreeListRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(RequestConstants.EMAIL.getValue(), email);
        ResponseEntity<WorkingTreeListRequest> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, WorkingTreeListRequest.class);
        return responseEntity.getBody();
    }
}
