package com.innopolis.innometrics.restapi.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionId implements Serializable {
    private String page;
    private String role;
}
