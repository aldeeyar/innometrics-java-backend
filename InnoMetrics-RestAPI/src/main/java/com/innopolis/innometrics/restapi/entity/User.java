package com.innopolis.innometrics.restapi.entity;

import com.innopolis.innometrics.restapi.dto.UserRequest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
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
    @Column
    private String birthday;
    @Column
    private String gender;
    @Column
    private String facebookAlias;
    @Column
    private String telegramAlias;
    @Column
    private String twitterAlias;
    @Column
    private String linkedinAlias;
    @Column(name = "isactive")
    private String isActive;
    @Column(name = "creationdate", insertable = false, updatable = false)
    private Date creationDate;
    @Column(name = "createdby", insertable = false, updatable = false)
    private String createdBy;
    @Column(name = "lastupdate", insertable = false)
    private Date lastUpdate;
    @Column(name = "updateby", insertable = false)
    private String updateBy;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "role", referencedColumnName = "name", insertable = false, updatable = false)
    private Role role;

    public User(UserRequest userRequest) {
        name = userRequest.getName();
        surname = userRequest.getSurname();
        email = userRequest.getEmail();
        password = userRequest.getPassword();
        birthday = userRequest.getBirthday();
        gender = userRequest.getGender();
        facebookAlias = userRequest.getFacebookAlias();
        telegramAlias = userRequest.getTelegramAlias();
        twitterAlias = userRequest.getTwitterAlias();
        linkedinAlias = userRequest.getLinkedinAlias();
        isActive = userRequest.getIsActive();
        confirmedAt = userRequest.getConfirmedAt();
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdate = new Date();
    }
}
