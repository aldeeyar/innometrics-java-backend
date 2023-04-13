package com.innopolis.innometrics.agentsgateway.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class ParamsConfigDTO implements Serializable {
    private String paramName;
    private String paramType;
    private String value;
}
