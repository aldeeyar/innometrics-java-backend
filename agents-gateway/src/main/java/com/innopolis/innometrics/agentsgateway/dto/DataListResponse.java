package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class DataListResponse {
    private List<DataConfigDTO> dataList;

    public void add(DataConfigDTO data) {
        this.dataList.add(data);
    }
}
