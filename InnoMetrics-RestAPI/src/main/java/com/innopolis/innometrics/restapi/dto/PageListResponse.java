package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageListResponse implements Serializable {
    private List<PageResponse> pageList;
}
