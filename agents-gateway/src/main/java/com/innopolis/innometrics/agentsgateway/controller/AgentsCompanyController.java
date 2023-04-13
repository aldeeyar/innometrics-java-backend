package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.AgentsCompanyDTO;
import com.innopolis.innometrics.agentsgateway.dto.AgentsCompanyListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentsXCompany;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import com.innopolis.innometrics.agentsgateway.service.AgentsXCompanyService;
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
public class AgentsCompanyController {
    private final AgentConfigService agentconfigService;
    private final AgentsXCompanyService agentsxcompanyService;

    @GetMapping(value = "/AgentsCompany", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyListResponse> getAgentsCompanyList() {
        List<AgentsXCompany> agentsCompanyList = agentsxcompanyService.getAgentsCompanyList();
        return convertToAgentsCompanyListResponseEntity(agentsCompanyList);
    }

    @GetMapping(value = "/AgentsCompany/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyDTO> getAgentsCompanyById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXCompany agentsxcompany = agentsxcompanyService.getAgentsCompanyById(id);
        if (agentsxcompany == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AgentsCompanyDTO response = convertToAgentsCompanyDto(agentsxcompany);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentsCompany/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyListResponse> getAgentsCompanyByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentsXCompany> agentsCompanyList = agentsxcompanyService.getAgentsCompanyByAgentId(id);
        return convertToAgentsCompanyListResponseEntity(agentsCompanyList);
    }

    @GetMapping(value = "/AgentsCompany/company/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyListResponse> getAgentsCompanyByCompanyId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AgentsXCompany> agentsCompanyList = agentsxcompanyService.getAgentsCompanyByCompanyId(id);
        return convertToAgentsCompanyListResponseEntity(agentsCompanyList);
    }

    @PostMapping(value = "/AgentsCompany", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyDTO> postAgentsCompany(@RequestBody AgentsCompanyDTO agentsCompanyDTO) {
        if (agentsCompanyDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXCompany agentsxcompany = convertToAgentsXCompanyEntity(agentsCompanyDTO);
        if (agentsxcompany == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            AgentsXCompany response = agentsxcompanyService.postAgentsCompany(agentsxcompany);
            return new ResponseEntity<>(convertToAgentsCompanyDto(response), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/AgentsCompany/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyDTO> putAgentsCompany(@PathVariable Integer id,
                                                             @RequestBody AgentsCompanyDTO agentsCompanyDTO) {
        if (id == null || agentsCompanyDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXCompany agentsxcompany = convertToAgentsXCompanyEntity(agentsCompanyDTO);
        if (agentsxcompany == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            AgentsXCompany response = agentsxcompanyService.putAgentsCompany(id, agentsxcompany);
            return new ResponseEntity<>(convertToAgentsCompanyDto(response),
                    response.getConfigid().equals(agentsCompanyDTO.getConfigId())
                            ? HttpStatus.OK
                            : HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/AgentsCompany/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyDTO> deleteAgentsCompanyById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXCompany deletedAgentsCompany = agentsxcompanyService.deleteAgentsCompanyById(id);
        return deletedAgentsCompany == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToAgentsCompanyDto(deletedAgentsCompany), HttpStatus.OK);
    }

    @DeleteMapping(value = "/AgentsCompany/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyListResponse> deleteAgentsCompanyByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentsXCompany> deletedAgentsCompanyList = agentsxcompanyService.deleteAgentsCompanyByAgentId(id);
        return convertToAgentsCompanyListResponseEntity(deletedAgentsCompanyList);
    }

    @DeleteMapping(value = "/AgentsCompany/company/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsCompanyListResponse> deleteAgentsCompanyByCompanyId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AgentsXCompany> deletedAgentsCompanyList = agentsxcompanyService.deleteAgentsCompanyByCompanyId(id);
        return convertToAgentsCompanyListResponseEntity(deletedAgentsCompanyList);
    }

    public ResponseEntity<AgentsCompanyListResponse> convertToAgentsCompanyListResponseEntity(
            List<AgentsXCompany> agentsCompanyList) {
        if (agentsCompanyList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AgentsCompanyListResponse responseList = new AgentsCompanyListResponse();
        for (AgentsXCompany agentsxcompany : agentsCompanyList) {
            responseList.add(this.convertToAgentsCompanyDto(agentsxcompany));
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public AgentsCompanyDTO convertToAgentsCompanyDto(AgentsXCompany agentsxcompany) {
        return new AgentsCompanyDTO(
                agentsxcompany.getConfigid(),
                agentsxcompany.getAgentId(),
                agentsxcompany.getCompanyId(),
                agentsxcompany.getKey(),
                agentsxcompany.getToken(),
                agentsxcompany.getIsActive(),
                agentsxcompany.getCreationdate(),
                agentsxcompany.getCreatedBy(),
                agentsxcompany.getLastupdate(),
                agentsxcompany.getUpdateBy());
    }

    public AgentsXCompany convertToAgentsXCompanyEntity(AgentsCompanyDTO agentsCompanyDTO) {
        Integer agentId = agentsCompanyDTO.getAgentId();
        Integer companyId = agentsCompanyDTO.getCompanyId();
        if (agentId == null || companyId == null) {
            return null;
        }
        AgentConfig agentconfig = this.agentconfigService.getAgentById(agentId);
        if (agentconfig == null) {
            return null;
        }
        AgentsXCompany agentsxcompany = new AgentsXCompany();
        agentsxcompany.setAgentConfig(agentconfig);
        agentsxcompany.setAgentId(agentId);
        agentsxcompany.setCompanyId(companyId);
        agentsxcompany.setKey(agentsCompanyDTO.getKey());
        agentsxcompany.setToken(agentsCompanyDTO.getToken());
        agentsxcompany.setIsActive(agentsCompanyDTO.getIsActive());
        return agentsxcompany;
    }
}
