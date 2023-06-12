package com.innopolis.innometrics.activitiescollector.controller;


import com.innopolis.innometrics.activitiescollector.config.JwtToken;
import com.innopolis.innometrics.activitiescollector.dto.*;
import com.innopolis.innometrics.activitiescollector.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Objects;

import static com.innopolis.innometrics.activitiescollector.constants.RequestConstants.API;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/V1/Classification", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClassificationConfig {
    private final CategoryService categoryService;
    private final JwtToken jwtTokenUtil;

    @PostMapping("/Category")
    public ResponseEntity<CategoryResponse> addCategory(@RequestBody CategoryRequest categoryRequest,
                                                        UriComponentsBuilder ucBuilder,
                                                        @RequestHeader(required = false) String token) {
        String userName = API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtTokenUtil.getUsernameFromToken(token);
        CategoryResponse response = categoryService.createCategory(categoryRequest, userName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/Category/GetAll")
    public ResponseEntity<CategoryListResponse> getAllCategories(@RequestHeader(required = false) String token) {
        CategoryListResponse response = categoryService.getAllCategories();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/Category")
    public ResponseEntity<CategoryResponse> getCategoryById(@RequestParam Integer categoryId,
                                                            @RequestHeader(required = false) String token) {
        CategoryResponse response = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/Category")
    public ResponseEntity<CategoryResponse> updateCategory(@RequestBody CategoryRequest categoryRequest,
                                                           UriComponentsBuilder ucBuilder,
                                                           @RequestHeader(required = false) String token) {
        String userName = API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtTokenUtil.getUsernameFromToken(token);
        CategoryResponse response = categoryService.updateCategory(categoryRequest, userName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/App")
    public ResponseEntity<AppCategoryResponse> addAppCategory(@RequestBody AppCategoryRequest appCategoryRequest,
                                                              UriComponentsBuilder ucBuilder,
                                                              @RequestHeader(required = false) String token) {
        String userName = API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtTokenUtil.getUsernameFromToken(token);
        AppCategoryResponse response = categoryService.createAppCategory(appCategoryRequest, userName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/App")
    public ResponseEntity<AppCategoryResponse> getAppCategoryById(@RequestParam Integer appId,
                                                                  @RequestHeader(required = false) String token) {
        AppCategoryResponse response = categoryService.getAppCategoryById(appId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/App")
    public ResponseEntity<AppCategoryResponse> updateAppCategory(@RequestBody AppCategoryRequest appCategoryRequest,
                                                                 UriComponentsBuilder ucBuilder,
                                                                 @RequestHeader(required = false) String token) {
        String userName = API.getValue();
        if (StringUtils.isNotEmpty(token))
            userName = jwtTokenUtil.getUsernameFromToken(token);
        AppCategoryResponse response = categoryService.updateAppCategory(appCategoryRequest, userName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/timeReport")
    public ResponseEntity<CategoriesTimeReportResponse> getTimeReport(@RequestParam(required = false) String projectID,
                                                                      @RequestParam(required = false) String email,
                                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date minDate,
                                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date maxDate) {
        projectID = Objects.equals(projectID, "") ? null : projectID;
        email = Objects.equals(email, "") ? null : email;
        TimeReportRequest request = new TimeReportRequest(projectID, email, minDate, maxDate);
        CategoriesTimeReportResponse myReport = categoryService.getTimeReport(request);
        return ResponseEntity.ok(myReport);
    }
}
