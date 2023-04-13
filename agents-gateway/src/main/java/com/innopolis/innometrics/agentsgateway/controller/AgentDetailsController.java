package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.DetailsConfigDTO;
import com.innopolis.innometrics.agentsgateway.dto.DetailsListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigDetails;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigDetailsService;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigMethodsService;
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
public class AgentDetailsController {
    private final AgentConfigMethodsService agentconfigmethodsService;
    private final AgentConfigDetailsService agentconfigdetailsService;

    @GetMapping(value = "/AgentDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsListResponse> getDetailsList() {
        List<AgentConfigDetails> detailsList = agentconfigdetailsService.getDetailsList();
        return convertToDetailsListResponseEntity(detailsList);
    }

    @GetMapping(value = "/AgentDetails/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsConfigDTO> getDetailsById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigDetails agentconfigdetails = agentconfigdetailsService.getDetailsById(id);
        if (agentconfigdetails == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DetailsConfigDTO response = convertToDetailsConfigDto(agentconfigdetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentDetails/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsListResponse> getDetailsByMethodId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigmethodsService.getMethodById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentConfigDetails> detailsList = agentconfigdetailsService.getDetailsByMethodId(id);
        return convertToDetailsListResponseEntity(detailsList);
    }

    @PostMapping(value = "/AgentDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsConfigDTO> postDetails(@RequestBody DetailsConfigDTO detailsConfigDTO) {
        if (detailsConfigDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigDetails agentconfigdetails = convertToAgentConfigDetailsEntity(detailsConfigDTO);
        if (agentconfigdetails == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigDetails response = agentconfigdetailsService.postDetails(agentconfigdetails);
        return new ResponseEntity<>(convertToDetailsConfigDto(response), HttpStatus.CREATED);
    }

    @PutMapping(value = "/AgentDetails/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsConfigDTO> putDetails(@PathVariable Integer id,
                                                       @RequestBody DetailsConfigDTO detailsConfigDTO) {
        if (id == null || detailsConfigDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigDetails agentconfigdetails = convertToAgentConfigDetailsEntity(detailsConfigDTO);
        if (agentconfigdetails == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigDetails response = agentconfigdetailsService.putDetails(id, agentconfigdetails);
        return new ResponseEntity<>(
                convertToDetailsConfigDto(response),
                response.getConfigDetId().equals(detailsConfigDTO.getConfigDetId())
                        ? HttpStatus.OK
                        : HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/AgentDetails/details/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsConfigDTO> deleteDetailsById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentConfigDetails deletedDetails = agentconfigdetailsService.deleteDetailsById(id);
        return deletedDetails == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToDetailsConfigDto(deletedDetails), HttpStatus.OK);
    }

    @DeleteMapping(value = "/AgentDetails/method/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailsListResponse> deleteDetailsByMethodId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigmethodsService.getMethodById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentConfigDetails> deletedDetailsList = agentconfigdetailsService.deleteDetailsByMethodId(id);
        return convertToDetailsListResponseEntity(deletedDetailsList);
    }

    public ResponseEntity<DetailsListResponse> convertToDetailsListResponseEntity(
            List<AgentConfigDetails> detailsList) {
        if (detailsList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        DetailsListResponse response = new DetailsListResponse();
        for (AgentConfigDetails data : detailsList) {
            response.add(this.convertToDetailsConfigDto(data));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public DetailsConfigDTO convertToDetailsConfigDto(AgentConfigDetails agentconfigdetails) {
        return new DetailsConfigDTO(
                agentconfigdetails.getConfigDetId(),
                agentconfigdetails.getAgentconfigmethods().getMethodId(),
                agentconfigdetails.getParamName(),
                agentconfigdetails.getParamType(),
                agentconfigdetails.getRequestParam(),
                agentconfigdetails.getRequestType(),
                agentconfigdetails.getIsActive(),
                agentconfigdetails.getDefaultValue(),
                agentconfigdetails.getCreationdate(),
                agentconfigdetails.getCreatedBy(),
                agentconfigdetails.getLastupdate(),
                agentconfigdetails.getUpdateBy());
    }

    public AgentConfigDetails convertToAgentConfigDetailsEntity(DetailsConfigDTO detailsConfigDTO) {
        Integer methodId = detailsConfigDTO.getMethodId();
        if (methodId == null) {
            return null;
        }
        AgentConfigMethods agentconfigmethods = agentconfigmethodsService.getMethodById(methodId);
        if (agentconfigmethods == null) {
            return null;
        }
        AgentConfigDetails agentconfigdetails = new AgentConfigDetails();
        agentconfigdetails.setAgentconfigmethods(agentconfigmethods);
        agentconfigdetails.setParamName(detailsConfigDTO.getParamName());
        agentconfigdetails.setParamType(detailsConfigDTO.getParamType());
        agentconfigdetails.setRequestParam(detailsConfigDTO.getRequestParam());
        agentconfigdetails.setRequestType(detailsConfigDTO.getRequestType());
        agentconfigdetails.setIsActive(detailsConfigDTO.getIsActive());
        agentconfigdetails.setDefaultValue(detailsConfigDTO.getDefaultValue());
        return agentconfigdetails;
    }
}
