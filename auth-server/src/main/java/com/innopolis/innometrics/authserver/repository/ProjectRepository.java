package com.innopolis.innometrics.authserver.repository;

import com.innopolis.innometrics.authserver.entitiy.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Project findByProjectID(Integer projectID);

    Boolean existsByProjectID(Integer projectID);

    @Query(value = "select * from innometricsauth.project p where p.isactive = 'Y'", nativeQuery = true)
    List<Project> findAllActive();
}

