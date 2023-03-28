package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompanyListRequest {
    private List<CompanyRequest> companyRequestList;
}
