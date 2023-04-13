package com.innopolis.innometrics.agentsgateway.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "agents_x_project")
@Data
public class AgentsXProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "configid")
    private Integer configId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agentid", updatable = false, insertable = false)
    private AgentConfig agentConfig;

    @Column(name = "agentid")
    private Integer agentId;

    @Column(name = "projectid")
    private Integer projectId;

    @Column
    private String key;

    @Column
    private String token;

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
