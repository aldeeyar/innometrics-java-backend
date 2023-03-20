package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.config.JwtToken;
import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.RoleResponse;
import com.innopolis.innometrics.restapi.dto.TeamListRequest;
import com.innopolis.innometrics.restapi.dto.UserRequest;
import com.innopolis.innometrics.restapi.dto.UserResponse;
import com.innopolis.innometrics.restapi.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Logger LOG = LogManager.getLogger();
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AuthAPI/User";
    private final RestTemplate restTemplate;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("User not found with email: " + email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    public boolean existsByEmail(String email) {
        User user = findByEmail(email);
        return user != null;
    }

    @HystrixCommand( commandKey = "findByEmail", fallbackMethod = "findByEmailFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public User findByEmail(String email) {
        UserRequest user;
        String uri = BASE_URL + "/" + email;
        ResponseEntity<UserRequest> response;
        response = restTemplate.exchange(uri, HttpMethod.GET, null, UserRequest.class);
        HttpStatus status = response.getStatusCode();
        user = response.getBody();
        if (status == HttpStatus.NO_CONTENT) return null;
        if (user != null) {
            return fromUserRequestToUser(user);
        } else return new User();
    }


    @HystrixCommand(commandKey = "update", fallbackMethod = "updateFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public Boolean update(User myUser) {
        UserRequest myUserRq = fromUserToUserRequest(myUser);
        HttpEntity<UserRequest> entity = new HttpEntity<>(myUserRq);
        ResponseEntity<UserRequest> response = restTemplate.exchange(BASE_URL, HttpMethod.PUT, entity, UserRequest.class);
        HttpStatus status = response.getStatusCode();
        return status == HttpStatus.CREATED;
    }

    public User mapUser(User myUser, UserRequest user, String token, JwtToken jwtTokenUtil) {
        String userName = token != null ? jwtTokenUtil.getUsernameFromToken(token) : "API";
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
        return myUser;
    }

    @HystrixCommand(commandKey = "create", fallbackMethod = "createFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public boolean create(UserRequest myUserRq) {
        try {
            HttpEntity<UserRequest> entity = new HttpEntity<>(myUserRq);
            ResponseEntity<UserRequest> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, UserRequest.class);
            HttpStatus status = response.getStatusCode();
            if (status == HttpStatus.CREATED) {
                return true;
            }
        }
        catch (Exception ex){
         LOG.warn(ex.getMessage());
        }
        return false;
    }


    @HystrixCommand( commandKey = "setRole", fallbackMethod = "setRoleFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public UserResponse setRole(String userName, String roleName) {
        String uri = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/User/Role";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("RoleName", roleName).queryParam("UserName", userName);
        ResponseEntity<UserResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.POST, null, UserResponse.class);
        return response.getBody();
    }


    private UserRequest fromUserToUserRequest(User myUser) {
        UserRequest myUserRq = new UserRequest();
        myUserRq.setName(myUser.getName());
        myUserRq.setSurname(myUser.getSurname());
        myUserRq.setEmail(myUser.getEmail());
        myUserRq.setPassword(myUser.getPassword());
        myUserRq.setBirthday(myUser.getBirthday());
        myUserRq.setGender(myUser.getGender());
        myUserRq.setFacebookAlias(myUser.getFacebookAlias());
        myUserRq.setTelegramAlias(myUser.getTelegramAlias());
        myUserRq.setTwitterAlias(myUser.getTwitterAlias());
        myUserRq.setLinkedinAlias(myUser.getLinkedinAlias());
        myUserRq.setIsActive(myUser.getIsActive());
        myUserRq.setConfirmedAt(myUser.getConfirmedAt());
        myUserRq.setRole(myUser.getRole().getName());
        return myUserRq;
    }

    private User fromUserRequestToUser(UserRequest userRequest) {
        User myUser = new User(userRequest);
        RoleResponse roleResponse = roleService.getRole(userRequest.getRole());
        myUser.setRole(roleService.roleFromRoleResponse(roleResponse));
        return myUser;
    }

    public Boolean updatePassword(User myUser, String token) {
        try {
            String uri = BASE_URL + "/" + myUser.getEmail();
            HttpHeaders headers = new HttpHeaders();
            headers.set(RequestConstants.TOKEN.getValue(), token);
            HttpEntity<String> entity = new HttpEntity<>(myUser.getPassword(), headers);
            try {
                ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Object.class);
                HttpStatus status = response.getStatusCode();
                return status == HttpStatus.OK;
            } catch (Exception e) {
                LOG.warn(e);
                return false;
            }
        }
        catch (Exception ex){
            LOG.warn(ex.getMessage());
        }
        return false;
    }


    @HystrixCommand(commandKey = "sendRessetPassordEmail", fallbackMethod = "sendRessetPassordEmailFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "600000")
    })
    public Boolean sendRessetPassordEmail(String userName, String backUrl, String token) {
        String uri = "http://INNOMETRICS-AUTH-SERVER/AuthAPI/User/" + userName + "/reset";
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<TeamListRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("BackUrl", backUrl);
        ResponseEntity<Boolean> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.POST, entity, Boolean.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "checkTemporalToken", fallbackMethod = "checkTemporalTokenFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "600000")
    })
    public Boolean checkTemporalToken(String userName, String temporalToken) {
        String uri = "http://INNOMETRICS-AUTH-SERVER/AuthAPI/User/" + userName + "/validate";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("TemporalToken", temporalToken);
        ResponseEntity<Boolean> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, null, Boolean.class);
        return response.getBody();
    }
}
