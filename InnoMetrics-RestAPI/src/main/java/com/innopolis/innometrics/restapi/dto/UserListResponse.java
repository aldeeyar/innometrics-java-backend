package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserListResponse implements Serializable {
    private List<UserResponse> userList;
}
