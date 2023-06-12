package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.dto.*;
import com.innopolis.innometrics.authserver.entitiy.Permission;
import com.innopolis.innometrics.authserver.entitiy.Project;
import com.innopolis.innometrics.authserver.entitiy.Role;
import com.innopolis.innometrics.authserver.entitiy.User;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.ProfileService;
import com.innopolis.innometrics.authserver.service.RoleService;
import com.innopolis.innometrics.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("AdminAPI")
@RequiredArgsConstructor
public class UserAPI {
    private final UserService userService;
    private final RoleService roleService;
    private final ProfileService profileService;

    @GetMapping("/Users")
    public ResponseEntity<UserListResponse> getActiveUsers(@RequestParam(required = false) String projectId) {
        UserListResponse response = userService.getActiveUsers(projectId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/Users/projects/{userName}")
    public ResponseEntity<ProjectListRequest> getProjectsByUsername(@PathVariable String userName) {
        if (!userName.isEmpty()) {
            User userDetails = userService.findByEmail(userName);
            if (userDetails == null)
                return ResponseEntity.noContent().build();
            ProjectListRequest response = new ProjectListRequest();
            for (Project entity : userDetails.getProjects()) {
                ProjectRequest pTemp = new ProjectRequest();
                BeanUtils.copyProperties(entity, pTemp);
                response.getProjectList().add(pTemp);
            }
            return ResponseEntity.ok(response);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }

    @GetMapping("/User/Permissions/{userName}")
    public ResponseEntity<PermissionResponse> getPermissions(@PathVariable String userName) {
        if (!userName.isEmpty()) {
            User userDetails = userService.findByEmail(userName);
            if (userDetails == null)
                return ResponseEntity.noContent().build();
            PermissionResponse response = new PermissionResponse();
            for (Permission p : userDetails.getRole().getPermissions()) {
                response.setRole(p.getRole());
                response.addPage(p.getPage());
            }
            return ResponseEntity.ok(response);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }

    @PostMapping("/User/Role")
    public ResponseEntity<UserResponse> setRoleToUser(@RequestParam String roleName,
                                                      @RequestParam String userName) {
        if (!userName.isEmpty()) {
            User userDetails = userService.findByEmail(userName);
            if (userDetails == null)
                return ResponseEntity.noContent().build();
            Role role = roleService.findByName(roleName);
            if (role == null) {
                throw new ValidationException(ExceptionMessage.NO_SUCH_ROLE.getValue());
            }
            userDetails.setRole(role);
            userService.save(userDetails);
            UserResponse userResponse = userService.fromUserToUserResponse(userDetails);
            return ResponseEntity.ok(userResponse);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }

    @PostMapping("/User/Profile")
    public ResponseEntity<ProfileRequest> updateProfileOfUser(@RequestBody ProfileRequest profileRequest,
                                                              @RequestHeader(required = false) String token) {
        if (profileRequest == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        if (profileRequest.getMacAddress() == null || profileRequest.getUserEmail() == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        ProfileRequest response;
        if (Boolean.FALSE.equals(profileService.existsByEmail(profileRequest.getUserEmail(), profileRequest.getMacAddress()))) {
            response = profileService.create(profileRequest);
        } else {
            response = profileService.update(profileRequest);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/User/Profile")
    public ResponseEntity<ProfileRequest> deleteProfile(@RequestParam Integer id,
                                                        @RequestHeader(required = false) String token) {
        if (id == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        profileService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/User/Profile")
    public ResponseEntity<ProfileRequest> findByMacaddress(@RequestParam String macaddress,
                                                           @RequestHeader(required = false) String token) {
        if (macaddress == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        ProfileRequest profileRequest = profileService.findByMacAddress(macaddress);
        if (profileRequest == null)
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(profileRequest);
    }
}
