package com.innopolis.innometrics.agentsgateway.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "externalproject_x_team")
@Data
public class ExternalProjectXTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "configid")
    private Integer configId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agentid")
    private AgentConfig agentConfig;

    @Column(insertable = false, updatable = false, name = "agentid")
    private Integer agentId;

    @Column(name = "teamid")
    private Integer teamId;

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
