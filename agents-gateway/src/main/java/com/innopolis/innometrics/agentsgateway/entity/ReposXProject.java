package com.innopolis.innometrics.agentsgateway.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "repos_x_project")
@Data
public class ReposXProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "configid")
    private Integer configId;

    @Column(name = "agentid")
    private Integer agentId;

    @Column(name = "projectid")
    private Integer projectId;

    @Column(name = "repoid")
    private String repoId;

    @Column(name = "isactive")
    private String isActive;

    @CreationTimestamp
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationdate;

    @CreatedBy
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;

    @UpdateTimestamp
    @Column(name = "lastupdate", insertable = false)
    private Date lastupdate;

    @LastModifiedBy
    @Column(name = "updateby", insertable = false)
    private String updateBy;
}
