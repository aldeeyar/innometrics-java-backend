package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.AgentsProjectDTO;
import com.innopolis.innometrics.agentsgateway.dto.AgentsProjectListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.AgentsXProject;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import com.innopolis.innometrics.agentsgateway.service.AgentsXProjectService;
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
public class AgentProjectController {

    private final AgentConfigService agentconfigService;
    private final AgentsXProjectService agentsxprojectService;

    @GetMapping(value = "/AgentsProject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectListResponse> getAgentsProjectList() {
        List<AgentsXProject> agentsProjectList = agentsxprojectService.getAgentsProjectList();
        return convertToAgentsProjectListResponseEntity(agentsProjectList);
    }

    @GetMapping(value = "/AgentsProject/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectDTO> getAgentsProjectById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXProject agentsxproject = agentsxprojectService.getAgentsProjectById(id);
        if (agentsxproject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AgentsProjectDTO response = convertToAgentsProjectDto(agentsxproject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/AgentsProject/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectListResponse> getAgentsProjectByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentsXProject> agentsProjectList = agentsxprojectService.getAgentsProjectByAgentId(id);
        return convertToAgentsProjectListResponseEntity(agentsProjectList);
    }

    @GetMapping(value = "/AgentsProject/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectListResponse> getAgentsProjectByProjectId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AgentsXProject> agentsProjectList = agentsxprojectService.getAgentsProjectByProjectId(id);
        return convertToAgentsProjectListResponseEntity(agentsProjectList);
    }

    @GetMapping(value = "/AgentsProject/config", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectDTO> getAgentsProjectByAgentIdAndProjectId(@RequestParam Integer agentid,
            @RequestParam Integer projectid) {
        if (agentid == null || projectid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXProject agentsxproject = agentsxprojectService.getAgentsProjectByAgentIdAndProjectId(agentid,
                projectid);
        if (agentsxproject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AgentsProjectDTO response = convertToAgentsProjectDto(agentsxproject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/AgentsProject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectDTO> postAgentsProject(@RequestBody AgentsProjectDTO agentsProjectDTO) {
        if (agentsProjectDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXProject agentsxproject = convertToAgentsXProjectEntity(agentsProjectDTO);
        if (agentsxproject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            AgentsXProject response = agentsxprojectService.postAgentsProject(agentsxproject);
            return new ResponseEntity<>(convertToAgentsProjectDto(response), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/AgentsProject/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectDTO> putAgentsProject(@PathVariable Integer id,
            @RequestBody AgentsProjectDTO agentsProjectDTO) {
        if (id == null || agentsProjectDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXProject agentsxproject = convertToAgentsXProjectEntity(agentsProjectDTO);
        if (agentsxproject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            AgentsXProject response = agentsxprojectService.putAgentsProject(id, agentsxproject);
            return new ResponseEntity<>(convertToAgentsProjectDto(response),
                    response.getConfigId().equals(agentsProjectDTO.getConfigId())
                            ? HttpStatus.OK
                            : HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/AgentsProject/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectDTO> deleteAgentsProjectById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        AgentsXProject deletedAgentsProject = agentsxprojectService.deleteAgentsProjectById(id);
        return deletedAgentsProject == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToAgentsProjectDto(deletedAgentsProject), HttpStatus.OK);
    }

    @DeleteMapping(value = "/AgentsProject/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectListResponse> deleteAgentsProjectByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<AgentsXProject> deletedAgentsProjectList = agentsxprojectService.deleteAgentsProjectByAgentId(id);
        return convertToAgentsProjectListResponseEntity(deletedAgentsProjectList);
    }

    @DeleteMapping(value = "/AgentsProject/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AgentsProjectListResponse> deleteAgentsProjectByProjectId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<AgentsXProject> deletedAgentsProjectList = agentsxprojectService.deleteAgentsProjectByProjectId(id);
        return convertToAgentsProjectListResponseEntity(deletedAgentsProjectList);
    }

    public ResponseEntity<AgentsProjectListResponse> convertToAgentsProjectListResponseEntity(
            List<AgentsXProject> agentsProjectList) {
        if (agentsProjectList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        AgentsProjectListResponse responseList = new AgentsProjectListResponse();
        for (AgentsXProject agentsxproject : agentsProjectList) {
            responseList.add(this.convertToAgentsProjectDto(agentsxproject));
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public AgentsProjectDTO convertToAgentsProjectDto(AgentsXProject agentsxproject) {
        return new AgentsProjectDTO(
                agentsxproject.getConfigId(),
                agentsxproject.getAgentId(),
                agentsxproject.getProjectId(),
                agentsxproject.getKey(),
                agentsxproject.getToken(),
                agentsxproject.getIsActive(),
                agentsxproject.getCreationdate(),
                agentsxproject.getCreatedBy(),
                agentsxproject.getLastupdate(),
                agentsxproject.getUpdateBy());
    }

    public AgentsXProject convertToAgentsXProjectEntity(AgentsProjectDTO agentsProjectDTO) {
        Integer agentId = agentsProjectDTO.getAgentId();
        Integer projectId = agentsProjectDTO.getProjectId();
        if (agentId == null || projectId == null) {
            return null;
        }
        AgentConfig agentconfig = this.agentconfigService.getAgentById(agentId);
        if (agentconfig == null) {
            return null;
        }
        AgentsXProject agentsxproject = new AgentsXProject();
        agentsxproject.setAgentConfig(agentconfig);
        agentsxproject.setAgentId(agentId);
        agentsxproject.setProjectId(projectId);
        agentsxproject.setKey(agentsProjectDTO.getKey());
        agentsxproject.setToken(agentsProjectDTO.getToken());
        agentsxproject.setIsActive(agentsProjectDTO.getIsActive());
        return agentsxproject;
    }
}
