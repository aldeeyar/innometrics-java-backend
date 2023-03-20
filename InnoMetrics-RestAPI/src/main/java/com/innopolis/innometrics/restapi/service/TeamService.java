package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.TeamListRequest;
import com.innopolis.innometrics.restapi.dto.TeamRequest;
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
public class TeamService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/Team";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "updateTeam", fallbackMethod = "updateTeamFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeamRequest updateTeam(TeamRequest teamRequest, String token) {
        String uri = BASE_URL + "/" + teamRequest.getTeamId();
        return uploadTeam(teamRequest, token, uri, HttpMethod.PUT);
    }

    public TeamRequest createTeam(TeamRequest teamRequest, String token) {
        return uploadTeam(teamRequest, token, BASE_URL, HttpMethod.POST);
    }

    private TeamRequest uploadTeam(TeamRequest teamRequest, String token, String uri, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeamRequest> entity = new HttpEntity<>(teamRequest, headers);
        ResponseEntity<TeamRequest> response = restTemplate
                .exchange(uri, method, entity, TeamRequest.class);
        HttpStatus status = response.getStatusCode();
        if (status == HttpStatus.NO_CONTENT) return null;
        return response.getBody();
    }

    public boolean deleteTeam(Integer id, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeamRequest> entity = new HttpEntity<>(headers);
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

    @HystrixCommand(commandKey = "getActiveTeams", fallbackMethod = "getActiveTeamsFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeamListRequest getActiveTeams(String token) {
        String uri = BASE_URL + "/active";
        return getTeams(token, uri);
    }

    @HystrixCommand(commandKey = "getAllTeams", fallbackMethod = "getAllTeamsFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeamListRequest getAllTeams(String token) {
        String uri = BASE_URL + "/all";
        return getTeams(token, uri);
    }

    private TeamListRequest getTeams(String token, String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        ResponseEntity<TeamListRequest> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, TeamListRequest.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getTeamsBy", fallbackMethod = "getTeamsByFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public TeamListRequest getTeamsBy(Integer teamId, Integer companyId, Integer projectId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeamListRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("teamId", teamId).queryParam("companyId", companyId).queryParam("projectId", projectId);
        ResponseEntity<TeamListRequest> responseEntity = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, TeamListRequest.class);
        return responseEntity.getBody();
    }
}

