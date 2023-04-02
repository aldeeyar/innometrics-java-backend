package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.*;
import com.innopolis.innometrics.authserver.entitiy.Page;
import com.innopolis.innometrics.authserver.entitiy.Permission;
import com.innopolis.innometrics.authserver.entitiy.Role;
import com.innopolis.innometrics.authserver.repository.PageRepository;
import com.innopolis.innometrics.authserver.repository.PermissionRepository;
import com.innopolis.innometrics.authserver.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PageRepository pageRepository;
    private final PermissionRepository permissionRepository;

    public PageListResponse getPagesWithIconsForRole(String role) {
        Set<Permission> permissions = roleRepository.findByName(role).getPermissions();
        PageListResponse pages = new PageListResponse();
        for (Permission permission : permissions) {
            PageResponse temp = new PageResponse(permission.getPage().getPage(), permission.getPage().getIcon());
            pages.addPageResponse(temp);
        }
        return pages;
    }

    public RoleResponse getRole(String roleName){
        Role role = roleRepository.findByName(roleName);
        if (role != null) {
            return roleResponseFromRole(role);
        }
        return null;
    }

    public RoleListResponse getRoles(){
        List<Role> roles = roleRepository.findAll();
        if (!roles.isEmpty()) {
            RoleListResponse roleResponseList = new RoleListResponse();
            for (Role role : roles) {
                roleResponseList.addRoleResponse(roleResponseFromRole(role));
            }
            return roleResponseList;
        }
        return null;
    }


    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = new Role();
        roleMapping(role, roleRequest);
        roleRepository.save(role);
        List<PageResponse> pages = roleRequest.getPages();
        Set<Permission> permissions = new HashSet<>();
        for (PageResponse pageRequest : pages) {
            Page page = pageRepository.findByPage(pageRequest.getPage());
            Permission permission = permissionRepository.findByPageAndRole(page, roleRequest.getName());
            if (page != null) {
                if (permission == null) {
                    permission = new Permission(page, roleRequest.getName());
                    permission = permissionRepository.save(permission);
                }
            } else {
                page = new Page(pageRequest.getPage(), pageRequest.getIcon());
                page = pageRepository.save(page);
                permission = new Permission(page, roleRequest.getName());
                permission = permissionRepository.save(permission);
            }
            permissions.add(permission);
        }
        role.setPermissions(permissions);
        role = roleRepository.save(role);
        return roleResponseFromRole(role);
    }

    @Transactional
    public RoleResponse updateRole(RoleRequest roleRequest) {
        Role role = roleRepository.findByName(roleRequest.getName());
        roleMapping(role, roleRequest);
        permissionRepository.deleteAllByRole(roleRequest.getName());
        List<PageResponse> pages = roleRequest.getPages();
        Set<Permission> permissions = new HashSet<>();
        for (PageResponse pageRequest : pages) {
            Page page = pageRepository.findByPage(pageRequest.getPage());
            Permission permission;
            if (page != null) {
                permission = new Permission(page, roleRequest.getName());
                permissionRepository.save(permission);
            } else {
                page = new Page(pageRequest.getPage(), pageRequest.getIcon());
                permission = new Permission(page, roleRequest.getName());
                permissionRepository.save(permission);
                pageRepository.save(page);
            }
            permissions.add(permission);
        }
        role.setPermissions(permissions);
        role = roleRepository.save(role);
        return roleResponseFromRole(role);
    }


    private RoleResponse roleResponseFromRole(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setName(role.getName());
        roleResponse.setCreatedBy(role.getCreatedBy());
        roleResponse.setCreationDate(role.getCreationDate());
        roleResponse.setDescription(role.getDescription());
        roleResponse.setIsActive(role.getIsActive());
        roleResponse.setLastUpdate(role.getLastUpdate());
        roleResponse.setUpdateBy(role.getUpdateBy());
        PageListResponse pages = new PageListResponse();
        for (Permission permission : role.getPermissions()) {
            PageResponse temp = new PageResponse(permission.getPage().getPage(), permission.getPage().getIcon());
            pages.addPageResponse(temp);
        }
        roleResponse.setPages(pages);
        return roleResponse;
    }

    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }

    public PermissionResponse createPermissions(PermissionResponse permissionResponse){
        PermissionResponse permissionResponseOut = new PermissionResponse();
        if(roleRepository.findByName(permissionResponse.getRole()) != null){
            permissionResponseOut.setRole(permissionResponse.getRole());
            List<Page> pages = permissionResponse.getPages();
            for (Page page : pages) {
                if(pageRepository.findByPage(page.getPage()) != null){
                    if(permissionRepository.findByPageAndRole(page,permissionResponse.getRole()) == null){
                        Permission permission = new Permission(page, permissionResponse.getRole());
                        permission = permissionRepository.save(permission);
                        Role role = roleRepository.findByName(permissionResponse.getRole());
                        if(!role.getPermissions().contains(permission)){
                            role.getPermissions().add(permission);
                            roleRepository.save(role);
                        }
                    }
                } else {
                    page = pageRepository.save(page);
                    Permission permission = new Permission(page, permissionResponse.getRole());
                    permissionRepository.save(permission);
                    Role role = roleRepository.findByName(permissionResponse.getRole());
                    role.getPermissions().add(permission);
                    roleRepository.save(role);
                }
                permissionResponseOut.addPage(page);
            }
            return permissionResponseOut;
        }
        return null;
    }

    private void roleMapping(Role role, RoleRequest roleRequest) {
        role.setName(roleRequest.getName());
        role.setCreatedBy(roleRequest.getCreatedBy());
        role.setCreationDate(roleRequest.getCreationDate());
        role.setDescription(roleRequest.getDescription());
        role.setIsActive(roleRequest.getIsActive());
        role.setLastUpdate(roleRequest.getLastUpdate());
        role.setUpdateBy(roleRequest.getUpdateBy());
    }
}
