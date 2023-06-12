package com.innopolis.innometrics.authserver.repository;

import com.innopolis.innometrics.authserver.entitiy.Teammembers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeammembersRepository extends JpaRepository<Teammembers, Integer> {
    List<Teammembers> findByTeamId(Integer teamid);

    List<Teammembers> findByEmail(String email);

    Teammembers findByEmailAndTeamId(String email, Integer teamId);

    Boolean existsByTeamIdAndEmail(Integer teamId, String email);

    Teammembers findByMemberId(Integer memberId);

    @Query(value = "select * from innometricsauth.teammembers p where p.isactive = 'Y'", nativeQuery = true)
    List<Teammembers> findAllActive();

}

