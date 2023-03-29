package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.config.JwtToken;
import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.constants.RequestConstants;
import com.innopolis.innometrics.authserver.dto.TeamListRequest;
import com.innopolis.innometrics.authserver.dto.TeamRequest;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("AdminAPI")
@RequiredArgsConstructor
public class TeamAPI {
    private final JwtToken jwtToken;
    private final TeamService teamService;

    @PostMapping("/Team")
    public ResponseEntity<TeamRequest> createTeam(@RequestBody TeamRequest teamRequest,
                                                  @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtToken.getUsernameFromToken(token);
        if (teamRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        teamRequest.setTeamId(null);
        teamRequest.setUpdateBy(userName);
        teamRequest.setCreatedBy(userName);
        TeamRequest response = teamService.create(teamRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/Team/{id}")
    public ResponseEntity<TeamRequest> updateTeam(@PathVariable Integer id,
                                                  @RequestBody TeamRequest teamRequest,
                                                  @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtToken.getUsernameFromToken(token);
        if (teamRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        teamRequest.setTeamId(id);
        teamRequest.setUpdateBy(userName);
        TeamRequest response;
        if (!teamService.existsById(teamRequest.getTeamId())) {
            teamRequest.setCreatedBy(userName);
            response = teamService.create(teamRequest);
        } else {
            response = teamService.update(teamRequest);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/Team")
    public ResponseEntity<TeamRequest> deleteTeam(@RequestParam Integer id,
                                                  @RequestHeader(required = false) String token) {
        if (id == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        try {
            teamService.delete(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            throw new ValidationException("Entries from 'externalproject_x_team' table with such teamid must be deleted firstly");
        }
    }

    @GetMapping("/Team/active")
    public ResponseEntity<TeamListRequest> findAllActiveTeams(@RequestHeader(required = false) String token) {
        return ResponseEntity.ok(
                teamService.findAllActiveTeams()
        );
    }

    @GetMapping("/Team/all")
    public ResponseEntity<TeamListRequest> findAllTeams(@RequestHeader(required = false) String token) {
        return ResponseEntity.ok(
                teamService.findAllTeams()
        );
    }

    @GetMapping("/Team")
    public ResponseEntity<TeamListRequest> findTeamBy(@RequestParam(required = false) Integer teamId,
                                                      @RequestParam(required = false) Integer companyId,
                                                      @RequestParam(required = false) Integer projectId,
                                                      @RequestHeader(required = false) String token) {
        if (teamId == null && companyId == null && projectId == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        return ResponseEntity.ok(
                teamService.findByTeamProperties(teamId, companyId, projectId)
        );
    }
}
