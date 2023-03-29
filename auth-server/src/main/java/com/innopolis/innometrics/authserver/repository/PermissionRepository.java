package com.innopolis.innometrics.authserver.repository;

import com.innopolis.innometrics.authserver.entitiy.Page;
import com.innopolis.innometrics.authserver.entitiy.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission findByPageAndRole(Page page, String role);

    void deleteAllByRole(String role);
}
