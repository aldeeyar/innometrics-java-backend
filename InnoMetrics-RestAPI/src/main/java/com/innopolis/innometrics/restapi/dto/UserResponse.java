package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserResponse implements Serializable {
    private String email;
    private String name;
    private String surname;
    private String isActive;
    private String role;
    private List<PageResponse> pages;
}
