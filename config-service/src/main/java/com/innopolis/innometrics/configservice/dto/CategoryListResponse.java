package com.innopolis.innometrics.configservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CategoryListResponse implements Serializable {
    private List<CategoryResponse> categoryList;

    public CategoryListResponse() {
        categoryList = new ArrayList<>();
    }

    public List<CategoryResponse> getCategoryList() {
        return categoryList;
    }
}
