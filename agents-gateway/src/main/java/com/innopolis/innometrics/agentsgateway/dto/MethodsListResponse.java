package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class MethodsListResponse {
    private List<MethodConfigDTO> methodsList;

    public void add(MethodConfigDTO methodConfigDTO) {
        this.methodsList.add(methodConfigDTO);
    }
}
