package com.innopolis.innometrics.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse implements Serializable {
    private String page;
    private String icon;
}
