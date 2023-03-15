package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserListRequest implements Serializable {
    private String projectID;
}
