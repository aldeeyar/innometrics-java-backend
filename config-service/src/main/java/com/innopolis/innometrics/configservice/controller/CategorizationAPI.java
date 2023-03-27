package com.innopolis.innometrics.configservice.controller;

import com.innopolis.innometrics.configservice.dto.CategoryListResponse;
import com.innopolis.innometrics.configservice.dto.CategoryRequest;
import com.innopolis.innometrics.configservice.dto.CategoryResponse;
import com.innopolis.innometrics.configservice.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("Categorization/Category")
@RequiredArgsConstructor
public class CategorizationAPI {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryListResponse> getCategories(@RequestHeader(required = false) String token) {
        CategoryListResponse response = categoryService.getCategoriesList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryResponse> getCategoriesById(@PathVariable Integer catId,
                                                              @RequestHeader(required = false) String token) {
        CategoryResponse response = categoryService.getCategoryByID(catId);
        if (response == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest category,
                                                           @RequestHeader(required = false) String token) {
        CategoryResponse response = categoryService.save(category);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest category,
                                                           @RequestHeader(required = false) String token) {
        return ResponseEntity.ok(categoryService.save(category));
    }

}
