package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserRequest implements Serializable {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String birthday;
    private String gender;
    private String facebookAlias;
    private String telegramAlias;
    private String twitterAlias;
    private String linkedinAlias;
    private Date confirmedAt;
    private String isActive;
    private String role;
}
