package com.innopolis.innometrics.restapi.dto;


import com.innopolis.innometrics.restapi.entity.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PermissionResponse implements Serializable {
    private List<Page> pages;
    private String role;
}
