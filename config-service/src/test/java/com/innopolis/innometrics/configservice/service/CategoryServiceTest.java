package com.innopolis.innometrics.configservice.service;

import com.innopolis.innometrics.configservice.dto.CategoryListResponse;
import com.innopolis.innometrics.configservice.dto.CategoryRequest;
import com.innopolis.innometrics.configservice.dto.CategoryResponse;
import com.innopolis.innometrics.configservice.entities.CategoryEntity;
import com.innopolis.innometrics.configservice.repository.CategoryRepository;
import com.innopolis.innometrics.configservice.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = {
        CategoryService.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryServiceTest {
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    private static final String TEST = "test";
    private static final String TEST1 = "test1";

    @Test
    void getCategoriesListEmptyRepositoryTest() {
        CategoryListResponse response = categoryService.getCategoriesList();
        assertEquals(0, response.getCategoryList().size());
    }

    @Test
    void getCategoriesListNonEmptyTest() {
        categoryRepository.save(createCategoryEntity());
        CategoryListResponse response = categoryService.getCategoriesList();
        assertEquals(1, response.getCategoryList().size());
    }

    @Test
    void getCategoryByIDEmptyRepositoryTest() {
        CategoryResponse response = categoryService.getCategoryByID(1);
        assertNull(response);
    }

    @Test
    void getCategoryByIDNonEmptyRepositoryTest() {
        categoryRepository.save(createCategoryEntity());
        CategoryResponse response = categoryService.getCategoryByID(1);
        assertEquals(TEST, response.getCreatedBy());
    }

    @Test
    void existsByNameNonEmptyRepositoryTest() {
        categoryRepository.save(createCategoryEntity());
        Boolean response = categoryService.existsByName(TEST);
        assertEquals(true, response);
    }

    @Test
    void existsByNameEmptyRepositoryTest() {
        Boolean response = categoryService.existsByName(TEST);
        assertEquals(false, response);
    }

    @Test
    void saveEmptyRepositoryTest() {
        CategoryResponse response = categoryService.save(createCategoryRequest());
        assertEquals(TEST1, response.getCatName());
    }

    @Test
    void saveNonEmptyRepositoryTest() {
        categoryRepository.save(createCategoryEntity());
        CategoryResponse response = categoryService.save(createCategoryRequest());
        assertEquals(TEST1, response.getCatName());
    }

    private CategoryEntity createCategoryEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setCatId(1);
        entity.setCatName(TEST);
        entity.setCatDescription(TEST);
        entity.setCreatedBy(TEST);
        return entity;
    }

    private CategoryRequest createCategoryRequest() {
        CategoryRequest request = new CategoryRequest();
        request.setCatId(1);
        request.setCatName(TEST1);
        request.setCatDescription(TEST);
        return request;
    }
}
