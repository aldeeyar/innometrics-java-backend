package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.dto.*;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminAPI")
@RequiredArgsConstructor
public class RoleAPI {
    private final RoleService roleService;

    @GetMapping("/Role/Permissions/{roleName}")
    public ResponseEntity<PageListResponse> getPages(@PathVariable String roleName) {
        PageListResponse pageListResponse = roleService.getPagesWithIconsForRole(roleName);
        if (pageListResponse == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(pageListResponse);
    }

    @GetMapping("/Role/{roleName}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable String roleName) {
        RoleResponse roleResponse = roleService.getRole(roleName);
        if (roleResponse == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(roleResponse);
    }

    @GetMapping("/Roles")
    public ResponseEntity<RoleListResponse> getRoles() {
        RoleListResponse roleResponseList = roleService.getRoles();
        if (roleResponseList == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(roleResponseList);
    }

    @PostMapping("/Role")
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest roleRequest,
                                                   @RequestHeader(required = false) String token) {
        if (roleService.getRole(roleRequest.getName()) != null)
            throw new ValidationException("Role already existed");
        RoleResponse roleResponse = roleService.createRole(roleRequest);
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }

    @PutMapping("/Role")
    public ResponseEntity<RoleResponse> updateRole(@RequestBody RoleRequest roleRequest,
                                                   @RequestHeader(required = false) String token) {
        if (roleRequest == null) {
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        }
        if (roleRequest.getName() == null || roleRequest.getPages() == null || roleRequest.getDescription() == null) {
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        }
        if (roleService.getRole(roleRequest.getName()) == null) {
            return ResponseEntity.notFound().build();
        }
        RoleResponse roleResponse = roleService.updateRole(roleRequest);
        return new ResponseEntity<>(roleResponse, HttpStatus.CREATED);
    }

    @PostMapping("/Role/Permissions")
    public ResponseEntity<PermissionResponse> createPermissions(@RequestBody PermissionResponse permissionResponse) {
        if (permissionResponse != null) {
            if (roleService.findByName(permissionResponse.getRole()) == null)
                return ResponseEntity.noContent().build();
            return ResponseEntity.ok(roleService.createPermissions(permissionResponse));
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }
}
