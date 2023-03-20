package com.innopolis.innometrics.restapi.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RequestConstants {

    TOKEN("Token"),
    EMAIL("email"),
    MIN_DATE("min_Date"),
    MAX_DATE("max_Date"),
    ID("id"),
    DATE_PATTERN("dd/MM/yyyy");

    private final String value;
}
