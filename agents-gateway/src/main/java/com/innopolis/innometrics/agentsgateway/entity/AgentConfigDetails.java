package com.innopolis.innometrics.agentsgateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agentconfigdetails")
public class AgentConfigDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "configdetid", updatable = false)
    private Integer configDetId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "methodid")
    private AgentConfigMethods agentconfigmethods;

    @Column(name = "paramname")
    private String paramName;

    @Column(name = "paramtype")
    private String paramType;

    @Column(name = "requestparam")
    private String requestParam;

    @Column(name = "requesttype")
    private String requestType;

    @Column(name = "isactive")
    private String isActive;

    @Column(name = "defaultvalue")
    private String defaultValue;

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
