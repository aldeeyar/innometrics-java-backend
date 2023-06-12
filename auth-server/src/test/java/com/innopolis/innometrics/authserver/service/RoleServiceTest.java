package com.innopolis.innometrics.authserver.service;

import com.innopolis.innometrics.authserver.dto.*;
import com.innopolis.innometrics.authserver.entitiy.Page;
import com.innopolis.innometrics.authserver.entitiy.Permission;
import com.innopolis.innometrics.authserver.entitiy.Role;
import com.innopolis.innometrics.authserver.repository.PageRepository;
import com.innopolis.innometrics.authserver.repository.PermissionRepository;
import com.innopolis.innometrics.authserver.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        RoleService.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class RoleServiceTest {
    private final static String TEST_DATA = "Test";
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PageRepository pageRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    RoleService roleService;

    @Test
    void getPagesWithIconsForRoleTest() {
        roleRepository.save(createRole());
        PageListResponse response = roleService.getPagesWithIconsForRole(TEST_DATA);
        assertNotNull(response);
    }

    @Test
    void getRolesTest() {
        roleRepository.save(createRole());
        RoleListResponse response = roleService.getRoles();
        assertNotNull(response);
    }

    @Test
    void getRoleTest() {
        roleRepository.save(createRole());
        RoleResponse response = roleService.getRole(TEST_DATA);
        assertNotNull(response);
    }

    @Test
    void createRoleTest() {
        pageRepository.save(createPage());
        permissionRepository.save(createPermission());
        RoleResponse response = roleService.createRole(createRoleRequest());
        assertNotNull(response);
    }

    @Test
    void findByNameNegativeTest() {
        Role role = roleService.findByName(TEST_DATA);
        assertNull(role);
    }

    @Test
    void findByNamePositiveTest() {
        roleRepository.save(createRole());
        Role role = roleService.findByName(TEST_DATA);
        assertNotNull(role);
    }

    @Test
    void createPermissionsTest() {
        pageRepository.save(createPage());
        permissionRepository.save(createPermission());
        PermissionResponse response = roleService.createPermissions(createPermissionResponse());
        assertNull(response);
    }

    private PermissionResponse createPermissionResponse() {
        ArrayList<Page> pages = new ArrayList<>();
        pages.add(createPage());
        PermissionResponse response = new PermissionResponse();
        response.setRole(TEST_DATA);
        response.setPages(pages);
        return response;
    }

    private Role createRole() {
        Role role = new Role();
        role.setCreatedBy(TEST_DATA);
        role.setName(TEST_DATA);
        role.setIsActive("Y");
        role.setDescription(TEST_DATA);
        return role;
    }

    private RoleRequest createRoleRequest() {
        ArrayList<PageResponse> pageResponses = new ArrayList<>();
        pageResponses.add(createPageResponse());
        RoleRequest request = new RoleRequest();
        request.setCreatedBy(TEST_DATA);
        request.setName(TEST_DATA);
        request.setIsActive("Y");
        request.setDescription(TEST_DATA);
        request.setPages(pageResponses);
        return request;
    }

    private PageResponse createPageResponse() {
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(TEST_DATA);
        pageResponse.setIcon(TEST_DATA);
        return pageResponse;
    }

    private Page createPage() {
        Page page = new Page();
        page.setPage(TEST_DATA);
        page.setIcon(TEST_DATA);
        return page;
    }

    private Permission createPermission() {
        Permission permission = new Permission();
        permission.setPage(createPage());
        permission.setRole(TEST_DATA);
        return permission;
    }
}
