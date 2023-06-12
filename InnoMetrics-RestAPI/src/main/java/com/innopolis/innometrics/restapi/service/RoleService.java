package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.dto.*;
import com.innopolis.innometrics.restapi.entity.Page;
import com.innopolis.innometrics.restapi.entity.Permission;
import com.innopolis.innometrics.restapi.entity.Role;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {
    private static final String BASE_URL = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/Role/";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "getPagesWithIconsForRole",
            fallbackMethod = "findPagesForRoleFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public List<Page> getPagesWithIconsForRole(String role) {
        PageListResponse pageListResponse;
        String uri = BASE_URL + "Permissions/" + role;
        ResponseEntity<PageListResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, PageListResponse.class);
        HttpStatus status = response.getStatusCode();
        pageListResponse = response.getBody();
        if (status == HttpStatus.NO_CONTENT) return Collections.emptyList();
        List<Page> pages = new ArrayList<>();
        if (pageListResponse != null && !pageListResponse.getPageList().isEmpty()) {
            for (PageResponse pageResponse : pageListResponse.getPageList()) {
                Page page = new Page();
                page.setIcon(pageResponse.getIcon());
                page.setPage(pageResponse.getPage());
                pages.add(page);
            }
        }
        return pages;
    }

    @HystrixCommand(commandKey = "getRole", fallbackMethod = "getRoleFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public RoleResponse getRole(String roleName) {
        String uri = BASE_URL + roleName;
        ResponseEntity<RoleResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, RoleResponse.class);
        HttpStatus status = response.getStatusCode();
        RoleResponse roleResponse = response.getBody();
        if (status == HttpStatus.NO_CONTENT) return null;
        return roleResponse;
    }

    @HystrixCommand(commandKey = "getRoles", fallbackMethod = "getRolesFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public RoleListResponse getRoles() {
        String uri = "http://INNOMETRICS-AUTH-SERVER/AdminAPI/Roles";
        ResponseEntity<RoleListResponse> response = restTemplate
                .exchange(uri, HttpMethod.GET, null, RoleListResponse.class);
        HttpStatus status = response.getStatusCode();
        RoleListResponse roleResponse = response.getBody();
        if (status == HttpStatus.NO_CONTENT) return null;
        return roleResponse;
    }

    @HystrixCommand(commandKey = "createRole", fallbackMethod = "createRoleFallback",
            commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")})
    public RoleResponse createRole(RoleRequest roleRequest) {
        HttpEntity<RoleRequest> entity = new HttpEntity<>(roleRequest);
        ResponseEntity<RoleResponse> response = restTemplate
                .exchange(BASE_URL, HttpMethod.POST, entity, RoleResponse.class);
        HttpStatus status = response.getStatusCode();
        RoleResponse restCall = response.getBody();
        if (status == HttpStatus.CREATED) {
            return restCall;
        }
        return null;
    }

    @HystrixCommand( commandKey = "updateRole", fallbackMethod = "updateRoleFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public RoleResponse updateRole(RoleRequest roleRequest) {
        HttpEntity<RoleRequest> entity = new HttpEntity<>(roleRequest);
        ResponseEntity<RoleResponse> response = restTemplate
                .exchange(BASE_URL, HttpMethod.PUT, entity, RoleResponse.class);
        HttpStatus status = response.getStatusCode();
        RoleResponse restCall = response.getBody();
        if (status == HttpStatus.CREATED) {
            return restCall;
        }
        return null;
    }

    public Role roleFromRoleResponse(RoleResponse roleResponse) {
        Role role = new Role();
        role.setName(roleResponse.getName());
        role.setCreatedBy(roleResponse.getCreatedBy());
        role.setCreationDate(roleResponse.getCreationDate());
        role.setDescription(roleResponse.getDescription());
        role.setIsActive(roleResponse.getIsActive());
        role.setLastUpdate(roleResponse.getLastUpdate());
        role.setUpdateBy(roleResponse.getUpdateBy());
        PageListResponse pageListResponse = roleResponse.getPages();
        Set<Permission> permissions = new HashSet<>();
        for (PageResponse pageResponse : pageListResponse.getPageList()) {
            Permission permission = new Permission();
            permission.setRole(roleResponse.getName());
            permission.setPage(new Page(pageResponse.getPage(), pageResponse.getIcon()));
            permissions.add(permission);
        }
        role.setPermissions(permissions);
        return role;
    }
}
