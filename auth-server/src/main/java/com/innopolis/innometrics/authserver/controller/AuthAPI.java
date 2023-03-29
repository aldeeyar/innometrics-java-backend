package com.innopolis.innometrics.authserver.controller;

import com.innopolis.innometrics.authserver.config.JwtToken;
import com.innopolis.innometrics.authserver.constants.ExceptionMessage;
import com.innopolis.innometrics.authserver.constants.RequestConstants;
import com.innopolis.innometrics.authserver.dto.UserRequest;
import com.innopolis.innometrics.authserver.entitiy.User;
import com.innopolis.innometrics.authserver.exceptions.ValidationException;
import com.innopolis.innometrics.authserver.service.RoleService;
import com.innopolis.innometrics.authserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("AuthAPI")
@RequiredArgsConstructor
public class AuthAPI {
    private final JwtToken jwtToken;
    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/User")
    public ResponseEntity<UserRequest> createUser(@RequestBody UserRequest user,
                                                  @RequestHeader(required = false) String token) {
        String userName = RequestConstants.API.getValue();
        if (user == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        if (user.getEmail() == null || user.getName() == null || user.getSurname() == null || user.getPassword() == null)
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
        if (Boolean.TRUE.equals(userService.existsByEmail(user.getEmail())))
            throw new ValidationException(ExceptionMessage.USERNAME_ALREADY_EXIST.getValue());
        if (roleService.findByName(user.getRole()) == null) {
            throw new ValidationException(ExceptionMessage.NO_SUCH_ROLE.getValue());
        }
        if (token != null)
            userName = jwtToken.getUsernameFromToken(token);
        User myUser = new User();
        myUser.setEmail(user.getEmail());
        myUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        myUser.setName(user.getName());
        myUser.setSurname(user.getSurname());
        myUser.setCreationDate(new Date());
        myUser.setCreatedBy(userName);
        myUser.setRole(roleService.findByName(user.getRole()));
        myUser.setIsActive("Y");
        myUser = userService.save(myUser);
        return new ResponseEntity<>(userService.fromUserToUserRequest(myUser), HttpStatus.CREATED);
    }

    @PutMapping("/User")
    public ResponseEntity<UserRequest> updateUser(@RequestBody UserRequest user,
                                                  @RequestHeader() String token) {
        if (user != null) {
            User myUser = userService.findByEmail(user.getEmail());
            if (roleService.findByName(user.getRole()) == null) {
                throw new ValidationException(ExceptionMessage.NO_SUCH_ROLE.getValue());
            }
            if (myUser != null) {
                String userName = token != null ? jwtToken.getUsernameFromToken(token) : RequestConstants.API.getValue();
                myUser.setEmail(user.getEmail());
                myUser.setName(user.getName());
                myUser.setSurname(user.getSurname());
                myUser.setBirthday(user.getBirthday());
                myUser.setGender(user.getGender());
                myUser.setFacebookAlias(user.getFacebookAlias());
                myUser.setTelegramAlias(user.getTelegramAlias());
                myUser.setTwitterAlias(user.getTwitterAlias());
                myUser.setLinkedinAlias(user.getLinkedinAlias());
                myUser.setUpdateBy(userName);
                myUser.setLastUpdate(new Date());
                myUser.setIsActive(user.getIsActive());
                myUser.setConfirmedAt(user.getConfirmedAt());
                myUser.setRole(roleService.findByName(user.getRole()));
                myUser = userService.save(myUser);
                return ResponseEntity.ok(userService.fromUserToUserRequest(myUser));
            }
            return ResponseEntity.notFound().build();
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }


    @PostMapping("/User/{userName}")
    public ResponseEntity<Boolean> updateUserPassword(@PathVariable String userName,
                                                      @RequestBody String password,
                                                      @RequestHeader() String token) {
        if (userName != null) {
            User myUser = userService.findByEmail(userName);
            if (myUser != null) {
                myUser.setPassword(new BCryptPasswordEncoder().encode(password));
                userService.save(myUser);
                return ResponseEntity.ok(true);
            }
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }

    @GetMapping("/User/{userName}")
    public ResponseEntity<UserRequest> getUserByName(@PathVariable String userName) {
        if (!userName.isEmpty()) {
            User userDetails = userService.findByEmail(userName);
            if (userDetails == null)
                return new ResponseEntity<>(new UserRequest(), HttpStatus.NO_CONTENT);
            UserRequest myUser = userService.fromUserToUserRequest(userDetails);
            return ResponseEntity.ok(myUser);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }

    @GetMapping("/Validate")
    public ResponseEntity<UserDetails> validateToken(@RequestHeader(required = false) String token) {
        String username = jwtToken.getUsernameFromToken(token);
        if (!username.isEmpty()) {
            UserDetails userDetails = userService.loadUserByUsername(username);
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }


    @PostMapping("/User/{userName}/reset")
    public ResponseEntity<Boolean> sendTemporalToken(@PathVariable String userName,
                                                     @RequestParam() String backUrl,
                                                     @RequestHeader() String token) {
        if (userName != null) {
            User myUser = userService.findByEmail(userName);
            if (myUser != null) {
                userService.sendRessetPasswordEmail(userName, backUrl);
                return ResponseEntity.ok(true);
            }
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }

    @GetMapping("/User/{userName}/validate")
    public ResponseEntity<Boolean> validateTemporalToken(@PathVariable String userName,
                                                         @RequestParam() String temporalToken) {
        if (userName != null) {
            User myUser = userService.findByEmail(userName);
            if (myUser != null && StringUtils.isNotEmpty(temporalToken)) {
                return ResponseEntity.ok(userService.checkTemporalToken(userName, temporalToken));
            }
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } else
            throw new ValidationException(ExceptionMessage.NOT_ENOUGH_DATA.getValue());
    }
}
