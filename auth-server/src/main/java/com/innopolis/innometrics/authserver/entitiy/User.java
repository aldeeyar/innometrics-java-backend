package com.innopolis.innometrics.authserver.entitiy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User implements Serializable {
    @Id
    @Column(updatable = false)
    private String email;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String surname;
    @Column(name = "confirmed_at", insertable = false, updatable = false)
    private Date confirmedAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_users",
            joinColumns = @JoinColumn(name = "email"),
            inverseJoinColumns = @JoinColumn(name = "projectid"))
    private Set<Project> projects;
    @Column
    private String birthday;
    @Column
    private String gender;
    @Column(name = "facebook_alias")
    private String facebookAlias;
    @Column(name = "telegram_alias")
    private String telegramAlias;
    @Column(name = "twitter_alias")
    private String twitterAlias;
    @Column(name = "linkedin_alias")
    private String linkedinAlias;
    @Column(name = "isactive")
    private String isActive;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "role", referencedColumnName = "name")
    private Role role;
    @PreUpdate
    public void preUpdate(){
        this.lastUpdate = new Date();
    }
}
