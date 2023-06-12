package com.innopolis.innometrics.configservice.service;

import com.innopolis.innometrics.configservice.dto.CategoryListResponse;
import com.innopolis.innometrics.configservice.dto.CategoryRequest;
import com.innopolis.innometrics.configservice.dto.CategoryResponse;
import com.innopolis.innometrics.configservice.entities.CategoryEntity;
import com.innopolis.innometrics.configservice.repository.CategoryRepository;
import com.innopolis.innometrics.configservice.services.CategoryServiceImpl;
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
        CategoryServiceImpl.class
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryServiceImplTest {
    @Autowired
    CategoryServiceImpl categoryServiceImpl;
    @Autowired
    CategoryRepository categoryRepository;

    private static final String TEST = "test";
    private static final String TEST1 = "test1";

    @Test
    void getCategoriesListEmptyRepositoryTest() {
        CategoryListResponse response = categoryServiceImpl.getCategoriesList();
        assertEquals(0, response.getCategoryList().size());
    }

    @Test
    void getCategoriesListNonEmptyTest() {
        categoryRepository.save(createCategoryEntity());
        CategoryListResponse response = categoryServiceImpl.getCategoriesList();
        assertEquals(1, response.getCategoryList().size());
    }

    @Test
    void getCategoryByIDEmptyRepositoryTest() {
        CategoryResponse response = categoryServiceImpl.getCategoryByID(1);
        assertNull(response);
    }

    @Test
    void getCategoryByIDNonEmptyRepositoryTest() {
        categoryRepository.save(createCategoryEntity());
        CategoryResponse response = categoryServiceImpl.getCategoryByID(1);
        assertEquals(TEST, response.getCreatedBy());
    }

    @Test
    void existsByNameNonEmptyRepositoryTest() {
        categoryRepository.save(createCategoryEntity());
        Boolean response = categoryServiceImpl.existsByName(TEST);
        assertEquals(true, response);
    }

    @Test
    void existsByNameEmptyRepositoryTest() {
        Boolean response = categoryServiceImpl.existsByName(TEST);
        assertEquals(false, response);
    }

    @Test
    void saveEmptyRepositoryTest() {
        CategoryResponse response = categoryServiceImpl.save(createCategoryRequest());
        assertEquals(TEST1, response.getCatName());
    }

    @Test
    void saveNonEmptyRepositoryTest() {
        categoryRepository.save(createCategoryEntity());
        CategoryResponse response = categoryServiceImpl.save(createCategoryRequest());
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
