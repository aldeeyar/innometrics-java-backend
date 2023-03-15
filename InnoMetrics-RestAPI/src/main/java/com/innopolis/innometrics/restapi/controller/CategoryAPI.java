package com.innopolis.innometrics.restapi.controller;

import com.innopolis.innometrics.restapi.dto.*;
import com.innopolis.innometrics.restapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping(value = "/V1/Admin/Classification", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryAPI {
    private final CategoryService categoryService;

    @GetMapping("/Category")
    public ResponseEntity<CategoryListResponse> getAllCategories(@RequestHeader(required = false) String token) {
        if (token == null) token = "";
        CategoryListResponse response = categoryService.getAllCategories(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/Category")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest,
                                                        UriComponentsBuilder ucBuilder,
                                                        @RequestHeader(required = false) String token) {

        if (token == null) token = "";
        CategoryResponse response = categoryService.addCategory(categoryRequest, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("Category/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer categoryId,
                                                            @RequestHeader(required = false) String token) {
        if (token == null) token = "";
        CategoryResponse response = categoryService.getCategoryById(categoryId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/Category")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest categoryRequest,
                                                           UriComponentsBuilder ucBuilder,
                                                           @RequestHeader(required = false) String token) {

        if (token == null) token = "";
        CategoryResponse response = categoryService.UpdateCategory(categoryRequest, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/App")
    public ResponseEntity<AppCategoryResponse> addAppCategory(@RequestBody AppCategoryRequest appCategoryRequest,
                                                              UriComponentsBuilder ucBuilder,
                                                              @RequestHeader(required = false) String token) {
        if (token == null) token = "";
        AppCategoryResponse response = categoryService.addAppCategory(appCategoryRequest, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/App/{appId}")
    public ResponseEntity<AppCategoryResponse> getAppCategoryById(@PathVariable Integer appId,
                                                                  @RequestHeader(required = false) String token) {

        if (token == null) token = "";
        AppCategoryResponse response = categoryService.getAppCategoryById(appId, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/App")
    public ResponseEntity<AppCategoryResponse> updateAppCategory(@RequestBody AppCategoryRequest appCategoryRequest,
                                                                 UriComponentsBuilder ucBuilder,
                                                                 @RequestHeader(required = false) String token) {
        if (token == null) token = "";
        AppCategoryResponse response = categoryService.UpdateAppCategory(appCategoryRequest, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
