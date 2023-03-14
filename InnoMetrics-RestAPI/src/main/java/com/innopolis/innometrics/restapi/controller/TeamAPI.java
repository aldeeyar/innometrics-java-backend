package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.DTO.TeamListRequest;
import com.innopolis.innometrics.restapi.DTO.TeamRequest;
import com.innopolis.innometrics.restapi.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/Team", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TeamAPI {
    private final TeamService teamService;

    @PostMapping()
    public ResponseEntity<TeamRequest> createTeam(@RequestBody TeamRequest teamRequest,
                                                  @RequestHeader(required = false) String token) {
        teamRequest.setTeamid(null);
        return new ResponseEntity<>(teamService.createTeam(teamRequest, token),
                HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamRequest> updateTeam(@PathVariable Integer id,
                                                  @RequestBody TeamRequest teamRequest,
                                                  @RequestHeader(required = false) String token) {
        teamRequest.setTeamid(id);
        return new ResponseEntity<>(teamService.updateTeam(teamRequest, token),
                HttpStatus.OK);
    }

    @DeleteMapping()
    public boolean deleteTeam(@RequestParam Integer id, @RequestHeader(required = false) String token) {
        return teamService.deleteTeam(id, token);
    }

    @GetMapping("/all")
    public ResponseEntity<TeamListRequest> findAllTeams(@RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teamService.getAllTeams(token),
                HttpStatus.OK
        );
    }

    @GetMapping("/active")
    public ResponseEntity<TeamListRequest> findAllActiveTeams(@RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teamService.getActiveTeams(token),
                HttpStatus.OK
        );
    }

    @GetMapping()
    public ResponseEntity<TeamListRequest> findTeamBy(@RequestParam(required = false) Integer teamId,
                                                      @RequestParam(required = false) Integer companyId,
                                                      @RequestParam(required = false) Integer projectId,
                                                      @RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teamService.getTeamsBy(teamId, companyId, projectId, token),
                HttpStatus.OK
        );
    }
}
