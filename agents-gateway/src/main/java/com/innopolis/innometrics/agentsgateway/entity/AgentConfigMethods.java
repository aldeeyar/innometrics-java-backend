package com.innopolis.innometrics.agentsgateway.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "agentconfigmethods")
@Data
public class AgentConfigMethods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "methodid", updatable = false)
    private Integer methodId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agentid")
    private AgentConfig agentConfig;

    @Column(insertable = false, updatable = false, name = "agentid")
    private Integer agentId;

    @Column
    private String endpoint;

    @Column
    private String description;

    @Column
    private String operation;

    @Column(name = "requesttype")
    private String requestType;

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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "methodid")
    private Set<AgentConfigDetails> params = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "methodid")
    private Set<AgentResponseConfig> responseParams = new HashSet<>();
}
