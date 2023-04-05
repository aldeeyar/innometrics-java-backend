package com.innopolis.innometrics.activitiescollector.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionMessage {
    NOT_ENOUGH_DATA("Not enough data provided"),
    BUG_NOT_UPDATED("Bug was not updated"),
    BUG_NOT_CREATED("Bug was not created"),
    BUG_DOES_NOT_EXIST("Bug with this id does not exist: "),
    BUG_ALREADY_EXIST("Bug with this id already exists: ");
    private final String value;
}
