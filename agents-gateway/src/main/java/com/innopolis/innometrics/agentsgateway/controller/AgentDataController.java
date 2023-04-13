package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.DataConfigDTO;
import com.innopolis.innometrics.agentsgateway.dto.DataListResponse;
import com.innopolis.innometrics.agentsgateway.dto.MethodConfigDTO;
import com.innopolis.innometrics.agentsgateway.dto.ParamsConfigDTO;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.entity.AgentDataConfig;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import com.innopolis.innometrics.agentsgateway.service.AgentDataConfigService;
import com.innopolis.innometrics.agentsgateway.service.CommonFieldsProcessor;
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
public class AgentDataController {
    private final AgentConfigService agentconfigService;
    private final AgentDataConfigService agentdataconfigService;

    @GetMapping(value = "/AgentData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataListResponse> getDataList() {
        List<AgentDataConfig> dataList = agentdataconfigService.getDataList();
        return convertToDataListResponseEntity(dataList);
    }

    @GetMapping(value = "/AgentData/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConfigDTO> getDataById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentDataConfig agentdataconfig = agentdataconfigService.getDataById(id);
        if (agentdataconfig == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DataConfigDTO response = convertToDataConfigDto(agentdataconfig);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentData/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataListResponse> getDataByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentDataConfig> dataList = agentdataconfigService.getDataAgentsByAgentId(id);
        return convertToDataListResponseEntity(dataList);
    }

    @PostMapping(value = "/AgentData", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConfigDTO> postData(@RequestBody DataConfigDTO dataConfigDTO) {
        if (dataConfigDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentDataConfig agentdataconfig = convertToAgentDataconfigEntity(dataConfigDTO);
        if (agentdataconfig == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentDataConfig response = agentdataconfigService.postData(agentdataconfig);
        return new ResponseEntity<>(convertToDataConfigDto(response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/AgentData/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConfigDTO> putData(@PathVariable Integer id,
                                                 @RequestBody DataConfigDTO dataConfigDTO) {
        if (id == null || dataConfigDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentDataConfig agentdataconfig = convertToAgentDataconfigEntity(dataConfigDTO);
        if (agentdataconfig == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentDataConfig response = agentdataconfigService.putData(id, agentdataconfig);
        return new ResponseEntity<>(convertToDataConfigDto(response),
                response.getDataConfigId().equals(dataConfigDTO.getDataConfigId())
                        ? HttpStatus.OK
                        : HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/AgentData/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataConfigDTO> deleteDataById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentDataConfig deletedData = agentdataconfigService.deleteDataById(id);
        return deletedData == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToDataConfigDto(deletedData), HttpStatus.OK);
    }

    @DeleteMapping(value = "/AgentData/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataListResponse> deleteDataByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentDataConfig> deletedDataList = agentdataconfigService.deleteDataByAgentId(id);
        return convertToDataListResponseEntity(deletedDataList);
    }


    public ResponseEntity<DataListResponse> convertToDataListResponseEntity(List<AgentDataConfig> dataList) {
        if (dataList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DataListResponse response = new DataListResponse();
        for (AgentDataConfig agentdataconfig : dataList) {
            response.add(convertToDataConfigDto(agentdataconfig));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public DataConfigDTO convertToDataConfigDto(AgentDataConfig agentdataconfig) {
        return new DataConfigDTO(
                agentdataconfig.getDataConfigId(),
                agentdataconfig.getAgentId(),
                agentdataconfig.getSchemaName(),
                agentdataconfig.getTableName(),
                agentdataconfig.getEventDateField(),
                agentdataconfig.getEventAuthorField(),
                agentdataconfig.getEventDescriptionField(),
                agentdataconfig.getIsActive(),
                agentdataconfig.getCreationdate(),
                agentdataconfig.getCreatedBy(),
                agentdataconfig.getLastupdate(),
                agentdataconfig.getUpdateBy(),
                agentdataconfig.getEventType());
    }

    public AgentDataConfig convertToAgentDataconfigEntity(DataConfigDTO dataConfigDTO) {
        Integer agentId = dataConfigDTO.getAgentId();
        if (agentId == null) {
            return null;
        }
        AgentConfig agentconfig = agentconfigService.getAgentById(agentId);
        if (agentconfig == null) {
            return null;
        }
        AgentDataConfig agentdataconfig = new AgentDataConfig();
        agentdataconfig.setAgentId(agentId);
        agentdataconfig.setAgentConfig(agentconfig);
        agentdataconfig.setSchemaName(dataConfigDTO.getSchemaName());
        agentdataconfig.setTableName(dataConfigDTO.getTableName());
        agentdataconfig.setEventDateField(dataConfigDTO.getEventDateField());
        agentdataconfig.setEventAuthorField(dataConfigDTO.getEventAuthorField());
        agentdataconfig.setEventDescriptionField(dataConfigDTO.getEventDescriptionField());
        agentdataconfig.setEventType(dataConfigDTO.getEventType());
        agentdataconfig.setIsActive(dataConfigDTO.getIsActive());
        return agentdataconfig;
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
}
