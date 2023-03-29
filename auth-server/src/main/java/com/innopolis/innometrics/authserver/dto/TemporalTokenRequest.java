package com.innopolis.innometrics.authserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TemporalTokenRequest {
    private String email;
    private String temporalToken;
    private Timestamp expirationDate;
}
