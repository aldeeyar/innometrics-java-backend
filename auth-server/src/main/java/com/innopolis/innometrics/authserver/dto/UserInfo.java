package com.innopolis.innometrics.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String email;
    private String name;
    private String surname;
    private String birthday;
    private String gender;
    private String facebookAlias;
    private String telegramAlias;
    private String twitterAlias;
    private String linkedinAlias;
    private String isActive;
    private String role;
}
