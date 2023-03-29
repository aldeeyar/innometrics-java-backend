package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.config.JwtToken;
import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.constants.RequestConstants;
import com.innopolis.innometrics.authserver.dto.TeammembersListRequest;
import com.innopolis.innometrics.authserver.dto.TeammembersRequest;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.TeammembersService;
import com.innopolis.innometrics.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminAPI/Teammember")
@RequiredArgsConstructor
public class TeamMemberAPI {
    private final TeammembersService teammembersService;
    private final UserService userService;
    private final JwtToken jwtToken;

    @PostMapping
    public ResponseEntity<TeammembersRequest> createTeammember(@RequestBody TeammembersRequest teammembersRequest,
                                                               @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtToken.getUsernameFromToken(token);
        if (teammembersRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        if (Boolean.FALSE.equals(userService.existsByEmail(teammembersRequest.getEmail())))
            throw new ValidationException(ExceptionMessage.NO_SUCH_USER.getValue());
        teammembersRequest.setMemberId(null);
        teammembersRequest.setUpdateBy(userName);
        teammembersRequest.setCreatedBy(userName);
        TeammembersRequest response = teammembersService.create(teammembersRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeammembersRequest> updateTeammember(@PathVariable Integer id,
                                                               @RequestBody TeammembersRequest teammembersRequest,
                                                               @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtToken.getUsernameFromToken(token);
        if (teammembersRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        if (Boolean.FALSE.equals(userService.existsByEmail(teammembersRequest.getEmail())))
            throw new ValidationException(ExceptionMessage.NO_SUCH_USER.getValue());
        teammembersRequest.setMemberId(id);
        teammembersRequest.setUpdateBy(userName);
        TeammembersRequest response;
        if (!(teammembersService.existsById(teammembersRequest.getMemberId())
                || teammembersService.existInTheTeam(teammembersRequest.getTeamId(), teammembersRequest.getEmail()))) {
            teammembersRequest.setCreatedBy(userName);
            response = teammembersService.create(teammembersRequest);
        } else {
            response = teammembersService.update(teammembersRequest);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<TeammembersRequest> deleteTeammember(@RequestParam Integer id,
                                                               @RequestHeader(required = false) String token) {
        if (id == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        teammembersService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/active")
    public ResponseEntity<TeammembersListRequest> findAllActiveTeammembers(@RequestHeader(required = false) String token) {
        return ResponseEntity.ok(
                teammembersService.findAllActiveTeammembers()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<TeammembersListRequest> findAllTeammembers(@RequestHeader(required = false) String token) {
        return ResponseEntity.ok(
                teammembersService.findAllTeammembers()
        );
    }

    @GetMapping
    public ResponseEntity<TeammembersListRequest> findTeammemberBy(@RequestParam(required = false) Integer memberId,
                                                                   @RequestParam(required = false) Integer teamId,
                                                                   @RequestParam(required = false) String email,
                                                                   @RequestHeader(required = false) String token) {
        if (teamId == null && memberId == null && email == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        return ResponseEntity.ok(
                teammembersService.findByTeammemberProperties(memberId, teamId, email)
        );
    }
}
