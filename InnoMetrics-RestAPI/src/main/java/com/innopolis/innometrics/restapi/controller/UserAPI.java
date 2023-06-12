package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.constants.ErrorMessages;
import com.innopolis.innometrics.restapi.dto.PermissionResponse;
import com.innopolis.innometrics.restapi.dto.UserRequest;
import com.innopolis.innometrics.restapi.dto.UserResponse;
import com.innopolis.innometrics.restapi.entity.User;
import com.innopolis.innometrics.restapi.exceptions.ValidationException;
import com.innopolis.innometrics.restapi.service.PermissionService;
import com.innopolis.innometrics.restapi.service.RoleService;
import com.innopolis.innometrics.restapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

import static com.innopolis.innometrics.restapi.constants.ErrorMessages.USERNAME_ALREADY_EXISTS;
import static com.innopolis.innometrics.restapi.constants.ErrorMessages.USERNAME_DOES_NOT_EXIST;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/User", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserAPI {
    private final RoleService roleService;
    private final UserService userService;
    private final PermissionService permissionService;
    @Autowired
    private JwtToken jwtTokenUtil;

    @GetMapping("/Permissions/{userName}")
    public ResponseEntity<PermissionResponse> getPermissionsOfUser(@PathVariable String userName,
                                                                   @RequestHeader(required = false) String token) {
        if (!userService.existsByEmail(userName)) {
            throw new ValidationException(USERNAME_DOES_NOT_EXIST.getMessage());
        }
        PermissionResponse permissionResponse = permissionService.getPermissionOfUser(userName);
        return new ResponseEntity<>(permissionResponse, HttpStatus.OK);
    }


    @PostMapping("/Role")
    public ResponseEntity<UserResponse> setRoleOfUser(@RequestParam String userName,
                                                      @RequestParam String roleName,
                                                      @RequestHeader(required = false) String token) {
        if (!userService.existsByEmail(userName)) {
            throw new ValidationException(USERNAME_DOES_NOT_EXIST.getMessage());
        }
        if (roleService.getRole(roleName) == null) {
            throw new ValidationException(ErrorMessages.ROLE_NOT_FOUND.getMessage());
        }
        UserResponse userResponse = userService.setRole(userName, roleName);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<UserRequest> createUser(@RequestBody @NotNull UserRequest user,
                                                  @RequestHeader(required = false) String token) {
        if (user.getEmail() == null || user.getName() == null || user.getSurname() == null || user.getPassword() == null) {
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
        }
        if (userService.existsByEmail(user.getEmail())) {
            throw new ValidationException(USERNAME_ALREADY_EXISTS.getMessage());
        }
        if (roleService.getRole(user.getRole()) == null) {
            throw new ValidationException(ErrorMessages.ROLE_NOT_FOUND.getMessage());
        }
        userService.create(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/UpdateStatus")
    public ResponseEntity<Boolean> updateUserStatus(@RequestParam @NotBlank String userId,
                                                    @RequestParam Boolean isActive,
                                                    @RequestHeader String token) {
        User myUser = userService.findByEmail(userId);
        if (myUser == null) {
            throw new ValidationException(USERNAME_DOES_NOT_EXIST.getMessage());
        }
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        myUser.setIsActive(Boolean.TRUE.equals(isActive) ? "Y" : "N");
        myUser.setLastUpdate(new Date());
        myUser.setUpdateBy(userName);
        Boolean response = userService.update(myUser) != null;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping()
    public ResponseEntity<Boolean> updateUser(@RequestBody UserRequest user,
                                              @RequestHeader(required = true) String token) {
        if (user != null) {
            User myUser = userService.findByEmail(user.getEmail());
            if (myUser != null) {
                User newUser = userService.mapUser(myUser, user, token, jwtTokenUtil);
                Boolean response = userService.update(newUser) != null;
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } else
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
    }


    @PostMapping("/{userName}")
    public ResponseEntity<Boolean> updateUserPassword(@PathVariable @NotBlank String userName,
                                                      @RequestBody String password,
                                                      @RequestHeader(required = true) String token) {
        User myUser = userService.findByEmail(userName);
        if (myUser != null) {
            myUser.setPassword(password);
            Boolean response = userService.updatePassword(myUser, token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{userName}/reset")
    public ResponseEntity<Boolean> sendTemporalToken(@PathVariable @NotBlank String userName,
                                                     @RequestParam() String backUrl,
                                                     @RequestHeader() String token) {
        return ResponseEntity.ok(userService.sendRessetPassordEmail(userName, backUrl, token));
    }

    @GetMapping("/{userName}/validate")
    public ResponseEntity<Boolean> validateTemporalToken(@PathVariable String userName,
                                                         @RequestParam(required = true) String temporalToken) {
        if (userName != null && temporalToken != null && !temporalToken.equals("")) {
            return ResponseEntity.ok(userService.checkTemporalToken(userName, temporalToken));
        } else
            throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private void handeException() {
        throw new ValidationException(ErrorMessages.NOT_ENOUGH_DATA.getMessage());
    }

}
