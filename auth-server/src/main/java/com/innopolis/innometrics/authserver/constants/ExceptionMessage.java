package com.innopolis.innometrics.authserver.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
    NOT_ENOUGH_DATA("Not enough data provided"),
    NO_SUCH_ROLE("No such role"),
    USERNAME_ALREADY_EXIST("Username already existed"),
    NO_SUCH_USER("No such user"),
    NO_COMPANY_FOUND("No company found by this id "),
    NO_PROJECT_FOUND("No project found by this id "),
    NO_PROFILE_FOUND("No profile found by id "),
    NO_TEAM_BY_ID_FOUND("No team found by id "),
    NO_TEAM_IN_PROJECT_FOUND("No teams found in this project "),
    NO_TEAM_MEMBER_FOUND("No team members found "),
    NO_TEAM_BY_EMAIL_FOUND("No team members found by this email ");

    private final String value;
}
