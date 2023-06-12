package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.AgentListResponse;
import com.innopolis.innometrics.agentsgateway.dto.AgentResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/AgentAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.DELETE})
@RequiredArgsConstructor
public class AgentController {

    private final AgentConfigService agentconfigService;

    @GetMapping(value = "/Agent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentListResponse> getAgentList(@RequestParam Integer projectId) {
        AgentListResponse response = agentconfigService.getAgentList(projectId);
        return response == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/Agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentResponse> getAgentById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfig agentconfig = agentconfigService.getAgentById(id);
        return agentconfig == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToAgentResponseDto(agentconfig), HttpStatus.OK);
    }

    @PostMapping(value = "/Agent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentResponse> postAgent(@RequestBody AgentResponse agent) {
        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfig agentconfig = convertToAgentConfigEntity(agent);
        AgentConfig response = agentconfigService.postAgent(agentconfig);
        return new ResponseEntity<>(convertToAgentResponseDto(response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/Agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentResponse> putAgent(@PathVariable Integer id, @RequestBody AgentResponse agent) {
        if (id == null || agent == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfig agentconfig = convertToAgentConfigEntity(agent);
        AgentConfig response = agentconfigService.putAgent(id, agentconfig);
        return new ResponseEntity<>(convertToAgentResponseDto(response),
                response.getAgentId().equals(agent.getAgentId())
                        ? HttpStatus.OK
                        : HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/Agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentResponse> deleteAgentById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfig deletedAgent = agentconfigService.deleteAgentById(id);
        return deletedAgent == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToAgentResponseDto(deletedAgent), HttpStatus.OK);
    }

    public AgentResponse convertToAgentResponseDto(AgentConfig agentConfig) {
        return new AgentResponse(
                agentConfig.getAgentId(),
                agentConfig.getAgentName(),
                agentConfig.getDescription(),
                agentConfig.getIsActive(),
                agentConfig.getCreationdate(),
                agentConfig.getCreatedBy(),
                agentConfig.getLastupdate(),
                agentConfig.getUpdateBy(),
                agentConfig.getSourceType(),
                agentConfig.getDbSchemaSource(),
                agentConfig.getRepoIdField(),
                agentConfig.getOauthUri(),
                agentConfig.getAuthenticationMethod(),
                agentConfig.getAccessTokenEndpoint(),
                agentConfig.getAuthorizationBaseUrl(),
                agentConfig.getRequestTokenEndpoint(),
                agentConfig.getApikey(),
                agentConfig.getApiSecret());
    }

    public AgentConfig convertToAgentConfigEntity(AgentResponse agentResponse) {
        AgentConfig agentconfig = new AgentConfig();
        agentconfig.setAgentName(agentResponse.getAgentName());
        agentconfig.setDescription(agentResponse.getDescription());
        agentconfig.setSourceType(agentResponse.getSourceType());
        agentconfig.setDbSchemaSource(agentResponse.getDbSchemaSource());
        agentconfig.setRepoIdField(agentResponse.getRepoIdField());
        agentconfig.setOauthUri(agentResponse.getAuthUri());
        agentconfig.setAuthenticationMethod(agentResponse.getAuthenticationMethod());
        agentconfig.setApikey(agentResponse.getApiKey());
        agentconfig.setApiSecret(agentResponse.getApiSecret());
        agentconfig.setAccessTokenEndpoint(agentResponse.getAccessTokenEndpoint());
        agentconfig.setAuthorizationBaseUrl(agentResponse.getAuthorizationBaseUrl());
        agentconfig.setRequestTokenEndpoint(agentResponse.getRequestTokenEndpoint());
        agentconfig.setIsActive(agentResponse.getIsConnected());
        return agentconfig;
    }
}
