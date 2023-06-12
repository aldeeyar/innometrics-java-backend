package com.innopolis.innometrics.authserver.dto;

import com.innopolis.innometrics.authserver.entitiy.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PermissionResponse {
    private List<Page> pages;
    private String role;

    public PermissionResponse() {
        pages = new ArrayList<>();
    }

    public void addPage(Page page){
        this.pages.add(page);
    }
}
