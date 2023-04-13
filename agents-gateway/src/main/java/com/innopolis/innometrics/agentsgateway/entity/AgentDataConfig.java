package com.innopolis.innometrics.agentsgateway.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "agent_data_config")
@Data
public class AgentDataConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "datacofingid")
    private Integer dataConfigId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agentid")
    private AgentConfig agentConfig;

    @Column(insertable = false, updatable = false, name = "agentid")
    private Integer agentId;

    @Column(name = "schemaname")
    private String schemaName;

    @Column(name = "tablename")
    private String tableName;

    @Column(name = "eventdatefield")
    private String eventDateField;

    @Column(name = "eventauthorfield")
    private String eventAuthorField;

    @Column(name = "eventdescriptionfield")
    private String eventDescriptionField;

    @Column(name = "eventtype")
    private String eventType;

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
