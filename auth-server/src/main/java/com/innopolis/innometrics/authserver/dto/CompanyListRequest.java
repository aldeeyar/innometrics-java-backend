package com.innopolis.innometrics.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CompanyListRequest {
    private List<CompanyRequest> companyRequestList;

    public CompanyListRequest() {
        companyRequestList = new ArrayList<>();
    }

    public void addCompanyRequest(CompanyRequest companyRequest){
        this.companyRequestList.add(companyRequest);
    }
}
