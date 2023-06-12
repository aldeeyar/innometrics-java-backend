package com.innopolis.innometrics.agentsgateway.controller;

import com.innopolis.innometrics.agentsgateway.dto.ExternalProjectTeamDTO;
import com.innopolis.innometrics.agentsgateway.dto.ExternalProjectTeamListResponse;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfig;
import com.innopolis.innometrics.agentsgateway.entity.ExternalProjectXTeam;
import com.innopolis.innometrics.agentsgateway.service.AgentConfigService;
import com.innopolis.innometrics.agentsgateway.service.ExternalProjectXTeamService;
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
public class ExternalProjectTeamController {
    private final AgentConfigService agentconfigService;
    private final ExternalProjectXTeamService externalprojectxteamService;

    @GetMapping(value = "/ExternalProjectTeam", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamListResponse> getExternalProjectTeamList() {
        List<ExternalProjectXTeam> externalProjectTeamList = externalprojectxteamService
                .getExternalProjectTeamList();
        return convertToExternalProjectTeamListResponseEntity(externalProjectTeamList);
    }

    @GetMapping(value = "/ExternalProjectTeam/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamDTO> getExternalProjectTeamById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ExternalProjectXTeam externalprojectxteam = externalprojectxteamService.getExternalProjectTeamById(id);
        if (externalprojectxteam == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExternalProjectTeamDTO response = convertToExternalProjectTeamDto(externalprojectxteam);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/ExternalProjectTeam/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamListResponse> getExternalProjectTeamByAgentId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ExternalProjectXTeam> externalProjectTeamList = externalprojectxteamService
                .getExternalProjectTeamByAgentId(id);
        return convertToExternalProjectTeamListResponseEntity(externalProjectTeamList);
    }

    @GetMapping(value = "/ExternalProjectTeam/team/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamListResponse> getExternalProjectTeamByTeamId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ExternalProjectXTeam> externalProjectTeamList = externalprojectxteamService
                .getExternalProjectTeamByTeamId(id);
        return convertToExternalProjectTeamListResponseEntity(externalProjectTeamList);
    }

    @PostMapping(value = "/ExternalProjectTeam", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamDTO> postExternalProjectTeam(
            @RequestBody ExternalProjectTeamDTO externalProjectTeamDTO) {
        if (externalProjectTeamDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ExternalProjectXTeam externalprojectxteam = convertToExternalprojectxteamEntity(externalProjectTeamDTO);
        if (externalprojectxteam == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            ExternalProjectXTeam response = externalprojectxteamService
                    .postExternalProjectTeam(externalprojectxteam);
            return new ResponseEntity<>(convertToExternalProjectTeamDto(response), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/ExternalProjectTeam/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamDTO> putExternalProjectTeam(@PathVariable Integer id,
                                                                         @RequestBody ExternalProjectTeamDTO externalProjectTeamDTO) {
        if (id == null || externalProjectTeamDTO == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ExternalProjectXTeam externalprojectxteam = convertToExternalprojectxteamEntity(externalProjectTeamDTO);
        if (externalprojectxteam == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            ExternalProjectXTeam response = externalprojectxteamService.putExternalProjectTeam(id,
                    externalprojectxteam);
            return new ResponseEntity<>(convertToExternalProjectTeamDto(response),
                    response.getConfigId().equals(externalProjectTeamDTO.getConfigid())
                            ? HttpStatus.OK
                            : HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/ExternalProjectTeam/config/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamDTO> deleteExternalProjectTeamById(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ExternalProjectXTeam deletedExternalProjectTeam = externalprojectxteamService
                .deleteExternalProjectTeamById(id);
        return deletedExternalProjectTeam == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(convertToExternalProjectTeamDto(deletedExternalProjectTeam), HttpStatus.OK);
    }

    @DeleteMapping(value = "/ExternalProjectTeam/agent/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamListResponse> deleteExternalProjectTeamByAgentId(
            @PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (agentconfigService.getAgentById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ExternalProjectXTeam> deletedExternalProjectTeamList = externalprojectxteamService
                .deleteExternalProjectTeamByAgentId(id);
        return convertToExternalProjectTeamListResponseEntity(deletedExternalProjectTeamList);
    }

    @DeleteMapping(value = "/ExternalProjectTeam/team/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExternalProjectTeamListResponse> deleteExternalProjectTeamByTeamId(@PathVariable Integer id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ExternalProjectXTeam> deletedExternalProjectTeamList = externalprojectxteamService
                .deleteExternalProjectTeamByTeamId(id);
        return convertToExternalProjectTeamListResponseEntity(deletedExternalProjectTeamList);
    }

    public ResponseEntity<ExternalProjectTeamListResponse> convertToExternalProjectTeamListResponseEntity(
            List<ExternalProjectXTeam> externalProjectTeamList) {
        if (externalProjectTeamList == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ExternalProjectTeamListResponse responseList = new ExternalProjectTeamListResponse();
        for (ExternalProjectXTeam externalprojectxteam : externalProjectTeamList) {
            responseList.add(this.convertToExternalProjectTeamDto(externalprojectxteam));
        }
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    public ExternalProjectTeamDTO convertToExternalProjectTeamDto(ExternalProjectXTeam externalprojectxteam) {
        return new ExternalProjectTeamDTO(
                externalprojectxteam.getConfigId(),
                externalprojectxteam.getAgentId(),
                externalprojectxteam.getTeamId(),
                externalprojectxteam.getRepoId(),
                externalprojectxteam.getIsActive(),
                externalprojectxteam.getCreationdate(),
                externalprojectxteam.getCreatedBy(),
                externalprojectxteam.getLastupdate(),
                externalprojectxteam.getUpdateBy());
    }

    public ExternalProjectXTeam convertToExternalprojectxteamEntity(ExternalProjectTeamDTO externalProjectTeamDTO) {
        Integer agentId = externalProjectTeamDTO.getAgentid();
        Integer teamId = externalProjectTeamDTO.getTeamid();
        if (agentId == null || teamId == null) {
            return null;
        }
        AgentConfig agentconfig = this.agentconfigService.getAgentById(agentId);
        if (agentconfig == null) {
            return null;
        }
        ExternalProjectXTeam externalprojectxteam = new ExternalProjectXTeam();
        externalprojectxteam.setAgentConfig(agentconfig);
        externalprojectxteam.setAgentId(agentId);
        externalprojectxteam.setTeamId(teamId);
        externalprojectxteam.setRepoId(externalProjectTeamDTO.getRepoid());
        externalprojectxteam.setIsActive(externalProjectTeamDTO.getIsactive());
        return externalprojectxteam;
    }
}
