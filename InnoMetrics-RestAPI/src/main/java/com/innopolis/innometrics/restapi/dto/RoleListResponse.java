package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleListResponse implements Serializable {
    private List<RoleResponse> roleList;
}
