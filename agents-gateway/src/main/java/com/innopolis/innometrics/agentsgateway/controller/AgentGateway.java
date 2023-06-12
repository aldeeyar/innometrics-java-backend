package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.ConnectProjectRequest;
import com.innopolis.innometrics.agentsgateway.dto.ProjectListResponse;
import com.innopolis.innometrics.agentsgateway.dto.RepoDataPerProjectResponse;
import com.innopolis.innometrics.agentsgateway.service.AgentsHandler;
import com.innopolis.innometrics.agentsgateway.service.OAuthService;
import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

import static com.innopolis.innometrics.agentsgateway.constants.HeaderConstants.*;

@RestController
@RequestMapping(value = "/AgentGateway", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT})
@RequiredArgsConstructor
public class AgentGateway {
    private final AgentsHandler agentsHandler;
    private final OAuthService oAuthService;

    @GetMapping("/projectList")
    public ResponseEntity<ProjectListResponse> getProjectList(@RequestParam Integer agentID,
                                                              @RequestParam Integer projectId,
                                                              UriComponentsBuilder ucBuilder) {
        ProjectListResponse response = null;
        try {
            response = agentsHandler.getProjectList(agentID, projectId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/connectProject")
    public ResponseEntity<Boolean> getConnectProject(@RequestBody ConnectProjectRequest request,
                                                     UriComponentsBuilder ucBuilder) {
        Boolean response = false;
        try {
            response = agentsHandler.getConnectProject(request);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (Boolean.TRUE.equals(response))
            return new ResponseEntity<>(true, HttpStatus.OK);
        else
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/projectData")
    public ResponseEntity<RepoDataPerProjectResponse> getProjectData(@RequestParam Integer projectId) {
        RepoDataPerProjectResponse response = agentsHandler.getRepoDataPerProject(projectId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/me/{agentId}/{projectId}")
    public void me(HttpServletResponse response, @PathVariable Integer agentId, @PathVariable Integer projectId,
                   @RequestParam String cb) {
        String authURL = oAuthService.getAuthorizationURL(agentId, projectId, cb);
        response.setHeader(LOCATION.getValue(), authURL);
        response.setHeader(ACCEPT.getValue(), APPLICATION_JSON.getValue());
        response.setStatus(302);
    }

    @GetMapping("/OAuth/{agentId}/{userid}")
    public void oAuth(HttpServletResponse response,
                      @PathVariable Integer agentId,
                      @PathVariable Integer userid,
                      @RequestParam String oauthToken,
                      @RequestParam String oauthVerifier,
                      @RequestParam String cb) {
        oAuthService.storeToken(agentId, userid, oauthVerifier, cb);
        response.setHeader(LOCATION.getValue(), cb);
        response.setHeader(ACCEPT.getValue(), APPLICATION_JSON.getValue());
        response.setStatus(302);
    }


    @GetMapping("/OAuth20/")
    public void oAuth20(HttpServletResponse response,
                        @RequestParam Integer agentId,
                        @RequestParam Integer projectId,
                        @RequestParam String code,
                        @RequestParam String cb) {
        oAuthService.storeToken(agentId, projectId, code, cb);
        response.setHeader(LOCATION.getValue(), cb);
        response.setHeader(ACCEPT.getValue(), APPLICATION_JSON.getValue());
        response.setStatus(302);
    }


    @GetMapping("/Simple/")
    public void oAuthSimple(HttpServletResponse response,
                            @RequestParam Integer agentId,
                            @RequestParam Integer projectId,
                            @RequestParam String code,
                            @RequestParam String cb) {
        oAuthService.storeToken(agentId, projectId, code, cb);
        response.setHeader(LOCATION.getValue(), cb);
        response.setHeader(ACCEPT.getValue(), APPLICATION_JSON.getValue());
        response.setStatus(302);
    }

    @PostMapping("/webhook")
    public Object webhookTest(HttpServletRequest request, @RequestParam String pathRule) throws IOException {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                headerNames.nextElement();
            }
        }
        Scanner s = new Scanner(request.getInputStream(), UTF.getValue()).useDelimiter("\\A");
        String jsonString = s.hasNext() ? s.next() : "";
        return JsonPath.parse(jsonString).read("$..conditions[metric, value, status]");
    }
}
