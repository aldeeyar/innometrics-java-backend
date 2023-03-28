package com.innopolis.innometrics.configservice.services;

import com.innopolis.innometrics.configservice.dto.CategoryListResponse;
import com.innopolis.innometrics.configservice.dto.CategoryRequest;
import com.innopolis.innometrics.configservice.dto.CategoryResponse;
import com.innopolis.innometrics.configservice.entities.CategoryEntity;
import com.innopolis.innometrics.configservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryListResponse getCategoriesList() {
        List<CategoryEntity> catList = categoryRepository.findAll();
        CategoryListResponse myResponse = new CategoryListResponse();
        for (CategoryEntity c : catList) {
            CategoryResponse myCat = createCategoryResponse(c);
            myResponse.getCategoryList().add(myCat);
        }
        return myResponse;
    }

    @Override
    public CategoryResponse getCategoryByID(Integer catId) {
        Optional<CategoryEntity> optional = categoryRepository.findById(catId);
        return optional.map(this::createCategoryResponse).orElse(null);
    }

    @Override

    public CategoryResponse save(CategoryRequest category) {
        CategoryEntity myCat = new CategoryEntity();
        if (Boolean.TRUE.equals(existsByName(category.getCatName()))) {
            Optional<CategoryEntity> optional = categoryRepository.findById(category.getCatId());
            if (optional.isPresent()) {
                myCat = optional.get();
            }
        }
        myCat.setCatName(category.getCatName());
        myCat.setCatDescription(category.getCatDescription());
        categoryRepository.save(myCat);
        return createCategoryResponse(myCat);
    }

    public Boolean existsByName(String catName) {
        return categoryRepository.existsByCatName(catName);
    }

    private CategoryResponse createCategoryResponse(CategoryEntity c) {
        CategoryResponse myCat = new CategoryResponse();
        myCat.setCatId(c.getCatId());
        myCat.setCatName(c.getCatName());
        myCat.setCatDescription(c.getCatDescription());
        myCat.setCreatedBy(c.getCreatedBy());
        myCat.setCreationDate(c.getCreationDate());
        myCat.setLastUpdate(c.getLastUpdate());
        myCat.setUpdateBy(c.getUpdateBy());
        return myCat;
    }
}
