package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.AgentConfigResponse;
import com.innopolis.innometrics.agentsgateway.dto.MethodConfigDTO;
import com.innopolis.innometrics.agentsgateway.dto.MethodsListResponse;
import com.innopolis.innometrics.agentsgateway.dto.ParamsConfigDTO;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigDetails;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigMethodsService;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import com.innopolis.innometrics.agentsgateway.service.CommonFieldsProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/AgentAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.DELETE})
@RequiredArgsConstructor
public class AgentConfigMethodsController {
    private final AgentConfigService agentconfigService;
    private final AgentConfigMethodsService agentconfigmethodsService;

    @GetMapping("/AgentConfiguration")
    public ResponseEntity<AgentConfigResponse> getAgentConfiguration(@RequestParam Integer agentId,
                                                                     @RequestParam(required = false) String callType) {
        AgentConfigResponse response = this.agentconfigService.getAgentConfig(agentId, callType);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentConfigMethods", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodsListResponse> getMethodsList() {
        List<AgentConfigMethods> methodsList = agentconfigmethodsService.getMethodsList();
        return convertToMethodsListResponseEntity(methodsList);
    }

    @GetMapping(value = "/AgentConfigMethods/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodConfigDTO> getMethodById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods agentconfigmethods = this.agentconfigmethodsService.getMethodById(id);
        if (agentconfigmethods == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MethodConfigDTO response = convertToMethodConfigDto(agentconfigmethods);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentConfigMethods/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodsListResponse> getMethodsByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentConfigMethods> methodsList = agentconfigmethodsService.getMethodsByAgentId(id);
        return convertToMethodsListResponseEntity(methodsList);
    }

    @GetMapping(value = "/AgentConfigMethodsOperation/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodConfigDTO> getMethodsByAgentIdAndOperation(@PathVariable Integer id,
                                                                           @RequestParam(value = "operation") String operation) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods response = agentconfigmethodsService.getMethodsByAgentidAndOperation(id, operation);
        return response == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToMethodConfigDto(response), HttpStatus.OK);
    }

    @PostMapping(value = "/AgentConfigMethods", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodConfigDTO> postMethod(@RequestBody MethodConfigDTO method) {
        if (method == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods agentconfigmethods = convertToAgentConfigMethodsEntity(method);
        if (agentconfigmethods == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods response = this.agentconfigmethodsService.postMethod(agentconfigmethods);
        return new ResponseEntity<>(convertToMethodConfigDto(response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/AgentConfigMethods/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodConfigDTO> putMethod(@PathVariable Integer id, @RequestBody MethodConfigDTO method) {
        if (id == null || method == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods agentconfigmethods = convertToAgentConfigMethodsEntity(method);
        if (agentconfigmethods == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods response = this.agentconfigmethodsService.putMethod(id, agentconfigmethods);
        return new ResponseEntity<>(convertToMethodConfigDto(response),
                response.getMethodId().equals(method.getMethodId())
                        ? HttpStatus.OK
                        : HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/AgentConfigMethods/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodConfigDTO> deleteMethodsById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigMethods agentConfigMethod = this.agentconfigmethodsService.deleteMethodById(id);
        if (agentConfigMethod == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MethodConfigDTO deletedMethod = convertToMethodConfigDto(agentConfigMethod);
        return new ResponseEntity<>(deletedMethod, HttpStatus.OK);
    }

    @DeleteMapping(value = "/AgentConfigMethods/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MethodsListResponse> deleteMethodsByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentConfigMethods> deletedMethodsList = this.agentconfigmethodsService.deleteMethodsByAgentId(id);
        return convertToMethodsListResponseEntity(deletedMethodsList);
    }


    public ResponseEntity<MethodsListResponse> convertToMethodsListResponseEntity(
            List<AgentConfigMethods> methodsList) {
        if (methodsList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        MethodsListResponse response = new MethodsListResponse();
        for (AgentConfigMethods agentConfigMethod : methodsList) {
            response.add(this.convertToMethodConfigDto(agentConfigMethod));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public MethodConfigDTO convertToMethodConfigDto(AgentConfigMethods agentConfigMethod) {
        MethodConfigDTO methodConfig = new MethodConfigDTO();
        methodConfig.setMethodId(agentConfigMethod.getMethodId());
        methodConfig.setAgentId(agentConfigMethod.getAgentId());
        methodConfig.setEndpoint(agentConfigMethod.getEndpoint());
        methodConfig.setDescription(agentConfigMethod.getDescription());
        methodConfig.setOperation(agentConfigMethod.getOperation());
        methodConfig.setIsActive(agentConfigMethod.getIsActive());
        methodConfig.setCreationdate(agentConfigMethod.getCreationdate());
        methodConfig.setCreatedBy(agentConfigMethod.getCreatedBy());
        methodConfig.setLastupdate(agentConfigMethod.getLastupdate());
        methodConfig.setUpdateBy(agentConfigMethod.getUpdateBy());
        methodConfig.setRequestType(agentConfigMethod.getRequestType());
        List<ParamsConfigDTO> parameters = CommonFieldsProcessor.createParameters(agentConfigMethod);
        methodConfig.setConfigParameters(parameters);
        return methodConfig;
    }

    public AgentConfigMethods convertToAgentConfigMethodsEntity(MethodConfigDTO methodConfigDTO) {
        Integer agentId = methodConfigDTO.getAgentId();
        if (agentId == null) {
            return null;
        }
        AgentConfig agentconfig = agentconfigService.getAgentById(agentId);
        if (agentconfig == null) {
            return null;
        }
        AgentConfigMethods agentconfigmethods = new AgentConfigMethods();
        agentconfigmethods.setAgentId(agentId);
        agentconfigmethods.setAgentConfig(agentconfig);
        agentconfigmethods.setEndpoint(methodConfigDTO.getEndpoint());
        agentconfigmethods.setDescription(methodConfigDTO.getDescription());
        agentconfigmethods.setOperation(methodConfigDTO.getOperation());
        agentconfigmethods.setIsActive(methodConfigDTO.getIsActive());
        agentconfigmethods.setRequestType(methodConfigDTO.getRequestType());
        List<ParamsConfigDTO> parameters = methodConfigDTO.getConfigParameters();
        if (parameters != null) {
            Set<AgentConfigDetails> params = new HashSet<>();
            for (ParamsConfigDTO paramsConfigDTO : parameters) {
                AgentConfigDetails agentconfigdetails = new AgentConfigDetails();
                agentconfigdetails.setParamName(paramsConfigDTO.getParamName());
                agentconfigdetails.setParamType(paramsConfigDTO.getParamType());
                params.add(agentconfigdetails);
            }
            agentconfigmethods.setParams(params);
        }
        return agentconfigmethods;
    }
}
