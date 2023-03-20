package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.ProjectListRequest;
import com.innopolis.innometrics.restapi.dto.ProjectRequest;
import com.innopolis.innometrics.restapi.dto.UserListResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AdminService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI";
    private static final String PROJECT_URL = "/Project";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "createProject",
            fallbackMethod = "createProjectFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public ProjectRequest createProject(ProjectRequest project, String token) {
        String uri = BASE_URL + PROJECT_URL;
        return uploadProject(project, token, uri, HttpMethod.POST);
    }

    @HystrixCommand(commandKey = "updateProject",
            fallbackMethod = "updateProjectFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public ProjectRequest updateProject(ProjectRequest project, String token) {
        String uri = BASE_URL + PROJECT_URL + "/" + project.getProjectID();
        return uploadProject(project, token, uri, HttpMethod.PUT);
    }

    private ProjectRequest uploadProject(ProjectRequest project, String token, String uri, HttpMethod method) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<ProjectRequest> entity = new HttpEntity<>(project, headers);
        try {
            ResponseEntity<ProjectRequest> response = restTemplate
                    .exchange(uri, method, entity, ProjectRequest.class);
            return response.getBody();
        } catch (Exception e) {
            LOG.warn(e);
            return new ProjectRequest();
        }
    }


    @HystrixCommand(commandKey = "deleteProject",
            fallbackMethod = "deleteProjectFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public void deleteProject(Integer projectId, String token) {
        String uri = BASE_URL + PROJECT_URL;
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<ProjectRequest> entity = new HttpEntity<>(null, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam(RequestConstants.ID.getValue(), projectId);
        try {
            ResponseEntity<ProjectRequest> response = restTemplate
                    .exchange(builder.toUriString(), HttpMethod.DELETE, entity, ProjectRequest.class);
            response.getBody();
        } catch (Exception e) {
            LOG.warn(e);
        }
    }

    @HystrixCommand(commandKey = "getProject",
            fallbackMethod = "getProjectFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public ProjectRequest getById(int id) {
        String uri = BASE_URL + PROJECT_URL + "/" + id;
        try {
            ResponseEntity<ProjectRequest> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, ProjectRequest.class);
            return response.getBody();
        } catch (Exception e) {
            LOG.warn(e);
            return null;
        }
    }
    @HystrixCommand(commandKey = "getActiveProjects",
            fallbackMethod = "getActiveProjectsFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public ProjectListRequest getActiveProjects() {
        String uri = BASE_URL + PROJECT_URL + "active";
        return getProjects(uri);
    }

    @HystrixCommand(commandKey = "getAllProjects",
            fallbackMethod = "getAllProjectsFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public ProjectListRequest getAllProjects() {
        String uri = BASE_URL + PROJECT_URL + "/all";
        return getProjects(uri);
    }

    private ProjectListRequest getProjects(String uri) {
        try {
            ResponseEntity<ProjectListRequest> response = restTemplate
                    .exchange(uri, HttpMethod.GET, null, ProjectListRequest.class);
            return response.getBody();
        } catch (Exception e) {
            LOG.warn(e);
            return null;
        }
    }

    @HystrixCommand(commandKey = "getActiveUsers",
            fallbackMethod = "getActiveUsersFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public UserListResponse getActiveUsers(String request) {
        String uri = BASE_URL + "/Users";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("projectId", request);
        try {
            ResponseEntity<UserListResponse> response = restTemplate
                    .exchange(builder.toUriString(), HttpMethod.GET, null, UserListResponse.class);
            return response.getBody();
        } catch (Exception e) {
            LOG.warn(e);
            return null;
        }
    }
    @HystrixCommand(commandKey = "getProjectsByUsername",
            fallbackMethod = "getProjectsByUsernameFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public ProjectListRequest getProjectsByUsername(String userName) {
        String uri = BASE_URL + "/Users/projects/" + userName;
        ResponseEntity<ProjectListRequest> response;
        response = restTemplate.exchange(uri, HttpMethod.GET, null, ProjectListRequest.class);
        return response.getBody();
    }
}
