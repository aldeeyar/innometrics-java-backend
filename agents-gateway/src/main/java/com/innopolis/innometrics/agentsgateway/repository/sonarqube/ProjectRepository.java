package com.innopolis.innometrics.agentsgateway.repository.sonarqube;

import com.innopolis.innometrics.agentsgateway.entity.sonarqube.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
