package com.innopolis.innometrics.agentsgateway.entity.sonarqube;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "project", schema = "sonarqube")
@Data
public class Project {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "projectid")
    private String projectId;

    @Column(name = "projectname")
    private String projectName;

    @Column(name = "providerid")
    private Long providerId;
}
