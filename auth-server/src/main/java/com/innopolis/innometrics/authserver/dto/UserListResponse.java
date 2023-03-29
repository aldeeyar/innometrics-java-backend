package com.innopolis.innometrics.authserver.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserListResponse implements Serializable {
    private List<UserResponse> userList;

    public UserListResponse() {
        userList = new ArrayList<>();
    }

    public List<UserResponse> getUserList() {
        return userList;
    }
}
