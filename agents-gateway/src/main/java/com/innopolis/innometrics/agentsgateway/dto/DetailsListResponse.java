package com.innopolis.innometrics.agentsgateway.dto;

import lombok.Data;

import java.util.List;

@Data
public class DetailsListResponse {
    private List<DetailsConfigDTO> detailsList;

    public void add(DetailsConfigDTO details) {
        this.detailsList.add(details);
    }
}
