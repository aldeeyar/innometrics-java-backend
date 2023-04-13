package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.ResponseConfigDTO;
import com.innopolis.innometrics.agentsgateway.dto.ResponseListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.entity.AgentResponseConfig;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigMethodsService;
import com.innopolis.innometrics.agentsgateway.service.AgentResponseConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/AgentAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.DELETE})
@RequiredArgsConstructor
public class AgentResponseController {
    private final AgentConfigMethodsService agentconfigmethodsService;
    private final AgentResponseConfigService agentresponseconfigService;

    @GetMapping(value = "/AgentResponse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseListResponse> getResponsesList() {
        List<AgentResponseConfig> responseList = agentresponseconfigService.getResponsesList();
        return convertToResponsesListResponseEntity(responseList);
    }

    @GetMapping(value = "/AgentResponse/response/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseConfigDTO> getResponseById(@PathVariable Integer id) {

        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentResponseConfig agentresponseconfig = agentresponseconfigService.getResponseById(id);
        if (agentresponseconfig == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ResponseConfigDTO response = convertToResponseConfigDto(agentresponseconfig);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentResponse/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseListResponse> getResponsesByMethodId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigmethodsService.getMethodById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentResponseConfig> responsesList = agentresponseconfigService.getResponsesByMethodId(id);
        return convertToResponsesListResponseEntity(responsesList);
    }

    @PostMapping(value = "/AgentResponse", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseConfigDTO> postResponse(@RequestBody ResponseConfigDTO responseConfigDTO) {
        if (responseConfigDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentResponseConfig agentresponseconfig = convertToAgentResponseConfigEntity(responseConfigDTO);
        if (agentresponseconfig == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentResponseConfig response = agentresponseconfigService.postResponse(agentresponseconfig);
        return new ResponseEntity<>(convertToResponseConfigDto(response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/AgentResponse/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseConfigDTO> putResponse(@PathVariable Integer id,
                                                         @RequestBody ResponseConfigDTO responseConfigDTO) {
        if (id == null || responseConfigDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentResponseConfig agentresponseconfig = convertToAgentResponseConfigEntity(responseConfigDTO);
        if (agentresponseconfig == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentResponseConfig response = agentresponseconfigService.putResponse(id, agentresponseconfig);
        return new ResponseEntity<>(convertToResponseConfigDto(response),
                response.getConfigResponseId().equals(responseConfigDTO.getConfigResponseId())
                        ? HttpStatus.OK
                        : HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/AgentResponse/response/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseConfigDTO> deleteResponseById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentResponseConfig deletedResponse = agentresponseconfigService.deleteResponseById(id);
        return deletedResponse == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToResponseConfigDto(deletedResponse), HttpStatus.OK);
    }

    @DeleteMapping(value = "/AgentResponse/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseListResponse> deleteResponseByMethodId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigmethodsService.getMethodById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentResponseConfig> deletedResponseList = agentresponseconfigService.deleteResponseByMethodId(id);
        return convertToResponsesListResponseEntity(deletedResponseList);
    }

    public ResponseEntity<ResponseListResponse> convertToResponsesListResponseEntity(
            List<AgentResponseConfig> responsesList) {
        if (responsesList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ResponseListResponse response = new ResponseListResponse();
        for (AgentResponseConfig responseConfig : responsesList) {
            response.add(this.convertToResponseConfigDto(responseConfig));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseConfigDTO convertToResponseConfigDto(AgentResponseConfig agentresponseconfig) {
        return new ResponseConfigDTO(
                agentresponseconfig.getConfigResponseId(),
                agentresponseconfig.getAgentconfigmethods().getMethodId(),
                agentresponseconfig.getResponseParam(),
                agentresponseconfig.getParamName(),
                agentresponseconfig.getParamType(),
                agentresponseconfig.getIsActive(),
                agentresponseconfig.getCreationdate(),
                agentresponseconfig.getCreatedBy(),
                agentresponseconfig.getLastupdate(),
                agentresponseconfig.getUpdateBy());
    }

    public AgentResponseConfig convertToAgentResponseConfigEntity(ResponseConfigDTO responseConfigDTO) {

        Integer methodId = responseConfigDTO.getMethodId();
        if (methodId == null) {
            return null;
        }
        AgentConfigMethods agentconfigmethods = this.agentconfigmethodsService.getMethodById(methodId);
        if (agentconfigmethods == null) {
            return null;
        }
        AgentResponseConfig agentresponseconfig = new AgentResponseConfig();
        agentresponseconfig.setAgentconfigmethods(agentconfigmethods);
        agentresponseconfig.setResponseParam(responseConfigDTO.getResponseParam());
        agentresponseconfig.setParamName(responseConfigDTO.getParamName());
        agentresponseconfig.setParamType(responseConfigDTO.getParamType());
        agentresponseconfig.setIsActive(responseConfigDTO.getIsActive());
        return agentresponseconfig;
    }
}
