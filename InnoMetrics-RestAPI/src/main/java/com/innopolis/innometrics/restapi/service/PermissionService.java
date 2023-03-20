package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.dto.PermissionResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/Role/Permissions";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "getPermissiosOfUser", fallbackMethod = "getPermissiosOfUserFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public PermissionResponse getPermissionOfUser(String userName) {
        PermissionResponse permissionResponse;
        String uri = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/User/Permissions/" + userName;
        ResponseEntity<PermissionResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, PermissionResponse.class);
        HttpStatus status = response.getStatusCode();
        permissionResponse = response.getBody();
        if (status == HttpStatus.NO_CONTENT) return null;
        return permissionResponse;
    }

    @HystrixCommand(commandKey = "createPermissios", fallbackMethod = "createPermissiosFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public PermissionResponse createPermission(PermissionResponse permissionResponse) {
        PermissionResponse permissionResponseOut;
        HttpEntity<PermissionResponse> entity = new HttpEntity<>(permissionResponse);
        ResponseEntity<PermissionResponse> response = restTemplate
                .exchange(BASE_URL, HttpMethod.POST, entity, PermissionResponse.class);
        HttpStatus status = response.getStatusCode();
        permissionResponseOut = response.getBody();
        if (status == HttpStatus.NO_CONTENT) return null;
        return permissionResponseOut;
    }
}
