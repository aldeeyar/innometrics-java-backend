package com.innopolis.innometrics.activitiescollector.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CategoryListResponse implements Serializable {
    private List<CategoryResponse> categories;
}
