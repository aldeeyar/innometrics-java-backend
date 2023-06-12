package com.innopolis.innometrics.agentsgateway.service;

import com.innopolis.innometrics.agentsgateway.dto.ParamsConfigDTO;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigDetails;
import com.innopolis.innometrics.agentsgateway.entity.AgentConfigMethods;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

public class CommonFieldsProcessor {
    private CommonFieldsProcessor() {

    }

    public static List<ParamsConfigDTO> createParameters(AgentConfigMethods agentConfigMethod) {
        List<ParamsConfigDTO> parameters = new ArrayList<>();
        for (AgentConfigDetails agentconfigdetails : agentConfigMethod.getParams()) {
            ParamsConfigDTO paramsConfigDTO = new ParamsConfigDTO();
            paramsConfigDTO.setParamName(agentconfigdetails.getParamName());
            paramsConfigDTO.setParamType(agentconfigdetails.getParamType());
            parameters.add(paramsConfigDTO);
        }
        return parameters;
    }

    public static HttpMethod getRequestType(String requestType) {
        switch (requestType) {
            case "GET":
                return HttpMethod.GET;
            case "POST":
                return HttpMethod.POST;
            case "PUT":
                return HttpMethod.PUT;
            default:
                return null;
        }
    }
}
