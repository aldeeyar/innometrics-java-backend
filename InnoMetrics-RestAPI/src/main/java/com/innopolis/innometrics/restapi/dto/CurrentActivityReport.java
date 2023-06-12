package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CurrentActivityReport {
    private List<String> appList;
    private List<String> macAddressList;
}
