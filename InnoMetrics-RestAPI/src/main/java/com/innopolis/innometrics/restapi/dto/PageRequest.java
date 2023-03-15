package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    private String page;
    private String icon;
}
