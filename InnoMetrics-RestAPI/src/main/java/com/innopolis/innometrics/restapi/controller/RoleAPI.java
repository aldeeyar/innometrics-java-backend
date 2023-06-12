package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.PermissionResponse;
import com.innopolis.innometrics.restapi.dto.RoleListResponse;
import com.innopolis.innometrics.restapi.dto.RoleRequest;
import com.innopolis.innometrics.restapi.dto.RoleResponse;
import com.innopolis.innometrics.restapi.entity.Page;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.service.PermissionService;
import com.innopolis.innometrics.restapi.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.innopolis.innometrics.restapi.constants.ErrorMessages.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/Role", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RoleAPI {
    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping("/Permissions/{role}")
    public ResponseEntity<List<Page>> listRolePermissions(@PathVariable String role) {
        if (roleService.getRole(role) == null) {
            throw new ValidationException(ROLE_NOT_FOUND.getMessage());
        }
        List<Page> lTemp = roleService.getPagesWithIconsForRole(role);
        if (lTemp.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lTemp, HttpStatus.OK);
    }

    @GetMapping("/{role}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String role,
                                                      @RequestHeader(required = false) String token) {
        RoleResponse roleResponse = roleService.getRole(role);
        if (roleResponse == null) {
            throw new ValidationException(ROLE_NOT_FOUND.getMessage());
        }
        return new ResponseEntity<>(roleResponse, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<RoleResponse> createRole(@RequestBody @NotNull RoleRequest roleRequest,
                                                   @RequestHeader(required = false) String token) {
        if (roleService.getRole(roleRequest.getName()) != null) {
            throw new ValidationException(ROLE_ALREADY_EXISTS.getMessage());
        }
        if (roleRequest.getName() == null || roleRequest.getPages() == null || roleRequest.getDescription() == null) {
            throw new ValidationException(NOT_ENOUGH_DATA.getMessage());
        }
        RoleResponse myRole = roleService.createRole(roleRequest);
        return new ResponseEntity<>(myRole, HttpStatus.CREATED);
    }

    @PostMapping("/Permissions")
    public ResponseEntity<PermissionResponse> createPermissions(@RequestBody @NotNull PermissionResponse permissionResponse,
                                                                @RequestHeader(required = false) String token) {
        if (roleService.getRole(permissionResponse.getRole()) == null) {
            throw new ValidationException(ROLE_NOT_FOUND.getMessage());
        }
        PermissionResponse permissionResponseOut = permissionService.createPermission(permissionResponse);
        return new ResponseEntity<>(permissionResponseOut, HttpStatus.CREATED);
    }


    @PutMapping()
    public ResponseEntity<RoleResponse> updateRole(@RequestBody @NotNull RoleRequest roleRequest,
                                                   @RequestHeader(required = false) String token) {
        if (roleRequest.getName() == null || roleRequest.getPages() == null || roleRequest.getDescription() == null) {
            throw new ValidationException(NOT_ENOUGH_DATA.getMessage());
        }
        if (roleService.getRole(roleRequest.getName()) == null) {
            RoleResponse myRole = roleService.createRole(roleRequest);
            return new ResponseEntity<>(myRole, HttpStatus.CREATED);
        }
        RoleResponse myRole = roleService.updateRole(roleRequest);
        return ResponseEntity.ok(myRole);
    }


    @GetMapping()
    public ResponseEntity<RoleListResponse> listAllRoles(@RequestHeader(required = false) String token) {
        RoleListResponse lTemp = roleService.getRoles();
        if (lTemp.getRoleList().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lTemp, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private void handeException() {
        throw new ValidationException(NOT_ENOUGH_DATA.getMessage());
    }
}
