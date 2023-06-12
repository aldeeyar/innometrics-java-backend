package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.ReposProjectDTO;
import com.innopolis.innometrics.agentsgateway.dto.ReposProjectListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.ReposXProject;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import com.innopolis.innometrics.agentsgateway.service.ReposXProjectService;
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
public class ReposProjectController {
    private final AgentConfigService agentconfigService;
    private final ReposXProjectService reposxprojectService;

    @GetMapping(value = "/ReposProject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectListResponse> getReposProjectList() {
        List<ReposXProject> reposProjectList = reposxprojectService.getReposProjectList();
        return this.convertToReposProjectListResponseEntity(reposProjectList);
    }


    @GetMapping(value = "/ReposProject/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectDTO> getReposProjectById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ReposXProject reposxproject = reposxprojectService.getReposProjectById(id);
        if (reposxproject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReposProjectDTO response = convertToReposProjectDto(reposxproject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/ReposProject/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectListResponse> getReposProjectByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ReposXProject> reposProjectList = reposxprojectService.getReposProjectByAgentId(id);
        return this.convertToReposProjectListResponseEntity(reposProjectList);
    }

    @GetMapping(value = "/ReposProject/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectListResponse> getReposProjectByProjectId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ReposXProject> reposProjectList = reposxprojectService.getReposProjectByProjectId(id);
        return this.convertToReposProjectListResponseEntity(reposProjectList);
    }

    @PostMapping(value = "/ReposProject", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectDTO> postReposProject(@RequestBody ReposProjectDTO reposProjectDTO) {
        if (reposProjectDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ReposXProject reposxproject = convertToReposxprojectEntity(reposProjectDTO);
        if (reposxproject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            ReposXProject response = reposxprojectService.postReposProject(reposxproject);
            return new ResponseEntity<>(convertToReposProjectDto(response), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/ReposProject/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectDTO> putReposProject(@PathVariable Integer id,
                                                           @RequestBody ReposProjectDTO reposProjectDTO) {
        if (id == null || reposProjectDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ReposXProject reposxproject = convertToReposxprojectEntity(reposProjectDTO);
        if (reposxproject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            ReposXProject response = reposxprojectService.putReposProject(id, reposxproject);
            return new ResponseEntity<>(convertToReposProjectDto(response),
                    response.getConfigId().equals(reposProjectDTO.getConfigId())
                            ? HttpStatus.OK
                            : HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/ReposProject/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectDTO> deleteReposProjectById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ReposXProject deletedReposProject = reposxprojectService.deleteReposProjectById(id);
        return deletedReposProject == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToReposProjectDto(deletedReposProject), HttpStatus.OK);
    }

    @DeleteMapping(value = "/ReposProject/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectListResponse> deleteReposProjectByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ReposXProject> deletedReposProjectList = reposxprojectService.deleteReposProjectByAgentId(id);
        return this.convertToReposProjectListResponseEntity(deletedReposProjectList);
    }

    @DeleteMapping(value = "/ReposProject/project/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReposProjectListResponse> deleteReposProjectByProjectId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ReposXProject> deletedReposProjectList = reposxprojectService.deleteReposProjectByProjectId(id);
        return this.convertToReposProjectListResponseEntity(deletedReposProjectList);
    }

    private ResponseEntity<ReposProjectListResponse> convertToReposProjectListResponseEntity(
            List<ReposXProject> reposProjectList) {
        if (reposProjectList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReposProjectListResponse responseList = new ReposProjectListResponse();
        for (ReposXProject reposxproject : reposProjectList) {
            responseList.add(convertToReposProjectDto(reposxproject));
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public ReposProjectDTO convertToReposProjectDto(ReposXProject reposxproject) {
        return new ReposProjectDTO(
                reposxproject.getConfigId(),
                reposxproject.getAgentId(),
                reposxproject.getProjectId(),
                reposxproject.getRepoId(),
                reposxproject.getIsActive(),
                reposxproject.getCreationdate(),
                reposxproject.getCreatedBy(),
                reposxproject.getLastupdate(),
                reposxproject.getUpdateBy());
    }

    public ReposXProject convertToReposxprojectEntity(ReposProjectDTO reposProjectDTO) {
        Integer agentId = reposProjectDTO.getAgentId();
        Integer projectId = reposProjectDTO.getProjectId();
        if (agentId == null || projectId == null) {
            return null;
        }
        AgentConfig agentconfig = this.agentconfigService.getAgentById(agentId);
        if (agentconfig == null) {
            return null;
        }
        ReposXProject reposxproject = new ReposXProject();
        reposxproject.setAgentId(agentId);
        reposxproject.setProjectId(projectId);
        reposxproject.setRepoId(reposProjectDTO.getRepoId());
        reposxproject.setIsActive(reposProjectDTO.getIsActive());
        return reposxproject;
    }
}
