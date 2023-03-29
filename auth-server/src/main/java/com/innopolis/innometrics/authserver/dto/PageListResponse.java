package com.innopolis.innometrics.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageListResponse {
    private List<PageResponse> pageList;

    public PageListResponse() {
        pageList = new ArrayList<>();
    }

    public void addPageResponse(PageResponse pageResponse){
        this.pageList.add(pageResponse);
    }
}
