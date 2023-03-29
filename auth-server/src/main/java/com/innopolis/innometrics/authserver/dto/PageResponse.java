package com.innopolis.innometrics.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class PageResponse implements Serializable {
    private String page;
    private String icon;
}
