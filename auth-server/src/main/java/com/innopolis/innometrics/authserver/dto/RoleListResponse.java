package com.innopolis.innometrics.authserver.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RoleListResponse  implements Serializable {
    private final List<RoleResponse> roleList;
    public RoleListResponse() {
        roleList = new ArrayList<>();
    }
    public void addRoleResponse(RoleResponse roleResponse){
        this.roleList.add(roleResponse);
    }
}
