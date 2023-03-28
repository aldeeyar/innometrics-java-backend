package com.innopolis.innometrics.configservice.services;

import com.innopolis.innometrics.configservice.dto.CategoryListResponse;
import com.innopolis.innometrics.configservice.dto.CategoryRequest;
import com.innopolis.innometrics.configservice.dto.CategoryResponse;

public interface CategoryService {
    CategoryListResponse getCategoriesList();

    CategoryResponse getCategoryByID(Integer catId);

    CategoryResponse save(CategoryRequest category);
}
