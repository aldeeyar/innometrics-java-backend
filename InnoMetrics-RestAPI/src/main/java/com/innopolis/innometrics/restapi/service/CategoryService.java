package com.innopolis.innometrics.restapi.service;

import com.innopolis.innometrics.restapi.constants.RequestConstants;
import com.innopolis.innometrics.restapi.dto.*;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private static final String BASE_URL = "http://INNOMETRICS-COLLECTOR-SERVER/V1/Classification";
    private static final String CATEGORY_URL = BASE_URL + "/Category";
    private static final String APP_URL = BASE_URL + "/App";
    private final RestTemplate restTemplate;

    @HystrixCommand(commandKey = "addCategory", fallbackMethod = "addCategoryFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CategoryResponse addCategory(CategoryRequest categoryRequest, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CATEGORY_URL);
        ResponseEntity<CategoryResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.POST, entity, CategoryResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "updateCategory", fallbackMethod = "updateCategoryFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CategoryResponse updateCategory(CategoryRequest categoryRequest, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<CategoryRequest> entity = new HttpEntity<>(categoryRequest, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CATEGORY_URL);
        ResponseEntity<CategoryResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.PUT, entity, CategoryResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getCategoryById", fallbackMethod = "getCategoryByIdFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CategoryResponse getCategoryById(Integer categoryId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<CategoryRequest> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(CATEGORY_URL)
                .queryParam("CategoryId", categoryId);
        ResponseEntity<CategoryResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CategoryResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "addAppCategory", fallbackMethod = "addAppCategoryFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public AppCategoryResponse addAppCategory(AppCategoryRequest appcategoryRequest, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<AppCategoryRequest> entity = new HttpEntity<>(appcategoryRequest, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(APP_URL);
        ResponseEntity<AppCategoryResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, AppCategoryResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "UpdateAppCategory", fallbackMethod = "UpdateAppCategoryFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public AppCategoryResponse updateAppCategory(AppCategoryRequest appcategoryRequest, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<AppCategoryRequest> entity = new HttpEntity<>(appcategoryRequest, headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(APP_URL);
        ResponseEntity<AppCategoryResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.PUT, entity, AppCategoryResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getAppCategoryById", fallbackMethod = "getAppCategoryByIdFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public AppCategoryResponse getAppCategoryById(Integer appId, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(APP_URL)
                .queryParam("AppId", appId);
        ResponseEntity<AppCategoryResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, AppCategoryResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getAllCategories", fallbackMethod = "getAllCategoriesFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CategoryListResponse getAllCategories(String token) {
        String uri = CATEGORY_URL + "/GetAll";
        HttpHeaders headers = new HttpHeaders();
        headers.set(RequestConstants.TOKEN.getValue(), token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        ResponseEntity<CategoryListResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CategoryListResponse.class);
        return response.getBody();
    }

    @HystrixCommand(commandKey = "getTimeReport", fallbackMethod = "getTimeReportFallback", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000")
    })
    public CategoriesTimeReportResponse getTimeReport(String projectID, String email, Date minDate, Date maxDate) {
        String uri = BASE_URL + "/timeReport";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(headers);
        Format formatter = new SimpleDateFormat("dd/MM/yyyy");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri)
                .queryParam("projectID", projectID)
                .queryParam(RequestConstants.EMAIL.getValue(), email)
                .queryParam(RequestConstants.MIN_DATE.getValue(), minDate != null ? formatter.format(minDate) : null)
                .queryParam(RequestConstants.MAX_DATE.getValue(), maxDate != null ? formatter.format(maxDate) : null);
        ResponseEntity<CategoriesTimeReportResponse> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, entity, CategoriesTimeReportResponse.class);
        return response.getBody();
    }
}
