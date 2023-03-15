package com.innopolis.innometrics.restapi.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageListResponse implements Serializable {
    List<PageResponse> pageList;

    public void addPageResponse(PageResponse pageResponse) {
        this.pageList.add(pageResponse);
    }
}
