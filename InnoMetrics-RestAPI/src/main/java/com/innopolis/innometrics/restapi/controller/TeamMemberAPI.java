package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.TeammembersListRequest;
import com.innopolis.innometrics.restapi.dto.TeammembersRequest;
import com.innopolis.innometrics.restapi.dto.WorkingTreeListRequest;
import com.innopolis.innometrics.restapi.service.TeammemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TeamMemberAPI {
    private final TeammemberService teammemberService;

    @PostMapping("/Teammember")
    public ResponseEntity<TeammembersRequest> createTeammember(@RequestBody TeammembersRequest teammembersRequest,
                                                               @RequestHeader(required = false) String token) {
        teammembersRequest.setMemberId(null);
        return new ResponseEntity<>(teammemberService.createTeammember(teammembersRequest, token),
                HttpStatus.OK);
    }

    @PutMapping("/Teammember/{id}")
    public ResponseEntity<TeammembersRequest> updateTeammember(@PathVariable Integer id,
                                                               @RequestBody TeammembersRequest teammembersRequest,
                                                               @RequestHeader(required = false) String token) {
        teammembersRequest.setMemberId(id);
        return new ResponseEntity<>(teammemberService.updateTeammember(teammembersRequest, token),
                HttpStatus.OK);
    }

    @DeleteMapping("/Teammember")
    public boolean deleteTeammember(@RequestParam Integer id, @RequestHeader(required = false) String token) {
        return teammemberService.deleteTeammember(id, token);
    }

    @GetMapping("/Teammember/active")
    public ResponseEntity<TeammembersListRequest> findAllActiveTeammembers(@RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teammemberService.getActiveTeammembers(token),
                HttpStatus.OK
        );
    }

    @GetMapping("/Teammember/all")
    public ResponseEntity<TeammembersListRequest> findAllTeammembers(@RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teammemberService.getAllTeammembers(token),
                HttpStatus.OK
        );
    }

    @GetMapping("/Teammember")
    public ResponseEntity<TeammembersListRequest> findTeammemberBy(@RequestParam(required = false) Integer memberId,
                                                                   @RequestParam(required = false) Integer teamId,
                                                                   @RequestParam(required = false) String email,
                                                                   @RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teammemberService.getTeammembersBy(memberId, teamId, email, token),
                HttpStatus.OK
        );
    }

    @GetMapping("/WorkingTree")
    public ResponseEntity<WorkingTreeListRequest> getWorkingTree(@RequestParam(required = false) String email,
                                                                 @RequestHeader(required = false) String token) {
        return new ResponseEntity<>(
                teammemberService.getWorkingTree(email, token),
                HttpStatus.OK
        );
    }
}
