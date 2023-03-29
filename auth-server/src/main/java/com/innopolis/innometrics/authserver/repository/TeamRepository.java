package com.innopolis.innometrics.authserver.repository;

import com.innopolis.innometrics.authserver.entitiy.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findByTeamId(Integer teamId);

    Boolean existsByTeamId(Integer teamId);

    @Query(value = "select * from innometricsauth.teams p where p.isactive = 'Y'", nativeQuery = true)
    List<Team> findAllActive();

    List<Team> findAllByCompanyId(Integer companyId);

    List<Team> findAllByProjectID(Integer projectId);

    List<Team> findAllByProjectIDAndCompanyId(Integer projectID, Integer companyId);
}

