package com.innopolis.innometrics.restapi.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorMessages {
    NOT_ENOUGH_DATA("Not enough data provided"),
    ROLE_NOT_FOUND("Such role doesn't exist"),
    MEASUREMENT_TYPE_EXISTS("Measurement type already existed"),
    USERNAME_DOES_NOT_EXIST("Username does not existed"),
    USERNAME_ALREADY_EXISTS("Username already existed"),
    ROLE_ALREADY_EXISTS("The role already existed"),
    PROJECT_DOES_NOT_EXIST("Project doesn't exist"),
    USER_DOES_NOT_EXIST("User doesn't exist"),
    BUG_NOT_UPDATED("Bug was not updated"),
    BUG_NOT_CREATED("Bug was not updated");

    private final String message;
}
