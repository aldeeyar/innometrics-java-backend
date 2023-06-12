package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.config.JwtToken;
import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.dto.*;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.CompanyService;
import com.innopolis.innometrics.authserver.service.ProjectService;
import com.innopolis.innometrics.authserver.service.TeamService;
import com.innopolis.innometrics.authserver.service.TeammembersService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminAPI")
@RequiredArgsConstructor
public class AdminAPI {
    private final ProjectService projectService;
    private final JwtToken jwtToken;
    private final CompanyService companyService;
    private final TeamService teamService;
    private final TeammembersService teammembersService;

    @GetMapping("/WorkingTree")
    public ResponseEntity<WorkingTreeListRequest> getWorkingTree(@RequestParam(required = false) String email,
                                                                 @RequestHeader(required = false) String token) {
        if (StringUtils.isNotEmpty(token))
            email = jwtToken.getUsernameFromToken(token);
        if (StringUtils.isNotEmpty(email))
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        WorkingTreeListRequest response = new WorkingTreeListRequest();
        TeammembersListRequest teammembersListRequest = teammembersService
                .findByTeammemberProperties(null, null, email);
        for (TeammembersRequest teammember : teammembersListRequest.getTeammembersRequestList()) {
            WorkingTreeRequest workingTreeRequest = new WorkingTreeRequest();
            workingTreeRequest.setEmail(email);
            workingTreeRequest.setMemberId(teammember.getMemberId());
            workingTreeRequest.setTeamId(teammember.getTeamId());
            TeamRequest team = teamService.findByTeamId(teammember.getTeamId());
            workingTreeRequest.setTeamName(team.getTeamName());
            workingTreeRequest.setTeamDescription(team.getDescription());
            workingTreeRequest.setCompanyId(team.getCompanyId());
            CompanyRequest company = companyService.findByCompanyId(team.getCompanyId());
            workingTreeRequest.setCompanyName(company.getCompanyName());
            workingTreeRequest.setProjectID(team.getProjectID());
            ProjectRequest project = projectService.getById(team.getProjectID());
            workingTreeRequest.setProjectname(project.getName());
            response.addWorkingTreeRequest(workingTreeRequest);
        }
        return ResponseEntity.ok(response);
    }
}
