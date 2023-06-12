package com.innopolis.innometrics.agentsgateway.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "agentresponseconfig")
@Data
public class AgentResponseConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "configresponseid", updatable = false)
    private Integer configResponseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "methodid")
    private AgentConfigMethods agentconfigmethods;

    @Column(name = "responseparam")
    private String responseParam;

    @Column(name = "paramname")
    private String paramName;

    @Column(name = "paramtype")
    private String paramType;

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
