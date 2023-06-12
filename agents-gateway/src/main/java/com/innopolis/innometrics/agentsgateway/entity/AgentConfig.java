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
@Table(name = "agentconfig")
@Data
public class AgentConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, name = "agentid")
    private Integer agentId;

    @Column(name = "agentname")
    private String agentName;

    @Column
    private String description;

    @Column(name = "sourcetype")
    private String sourceType;

    @Column(name = "dbschemasource")
    private String dbSchemaSource;

    @Column(name = "repoidfield")
    private String repoIdField;

    @Column(name = "oauthuri")
    private String oauthUri;

    @Column(name = "authenticationmethod")
    private String authenticationMethod;

    @Column
    private String apikey;

    @Column(name = "apisecret")
    private String apiSecret;

    @Column(name = "accesstokenendpoint")
    private String accessTokenEndpoint;

    @Column(name = "authorizationbaseurl")
    private String authorizationBaseUrl;

    @Column(name = "requesttokenendpoint")
    private String requestTokenEndpoint;

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
    @JoinColumn(name = "agentid")
    private Set<AgentConfigMethods> methods = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "agentid")
    private Set<AgentDataConfig> dataconfig = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "agentid")
    private Set<ReposXProject> reposconfig = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "agentid")
    private Set<AgentsXProject> agentsProject = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "agentid")
    private Set<ExternalProjectXTeam> externalProjectTeam = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "agentid")
    private Set<AgentsXCompany> agentsCompany = new HashSet<>();
}
