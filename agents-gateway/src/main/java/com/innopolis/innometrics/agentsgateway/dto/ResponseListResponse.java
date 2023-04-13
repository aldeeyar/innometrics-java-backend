package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseListResponse {
    private List<ResponseConfigDTO> responseList;

    public void add(ResponseConfigDTO response) {
        this.responseList.add(response);
    }
}
