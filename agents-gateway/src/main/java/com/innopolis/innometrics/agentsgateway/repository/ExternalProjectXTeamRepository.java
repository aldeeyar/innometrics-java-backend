package com.innopolis.innometrics.agentsgateway.repository;

import com.innopolis.innometrics.agentsgateway.entity.ExternalProjectXTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalProjectXTeamRepository extends JpaRepository<ExternalProjectXTeam, Integer> {
}
