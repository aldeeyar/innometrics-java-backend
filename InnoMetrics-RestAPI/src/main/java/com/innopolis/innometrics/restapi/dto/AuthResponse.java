package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwtToken;
}
