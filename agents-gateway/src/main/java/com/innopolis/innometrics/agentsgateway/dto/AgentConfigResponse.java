package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class AgentConfigResponse {
    private List<MethodConfigDTO> methodsConfig;

    public void addMethodConfig(MethodConfigDTO methodConfig) {
        methodsConfig.add(methodConfig);
    }
}
