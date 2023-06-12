package com.innopolis.innometrics.activitiescollector.service;

import com.innopolis.innometrics.activitiescollector.dto.*;
import com.innopolis.innometrics.activitiescollector.entity.ActAppxCategory;
import com.innopolis.innometrics.activitiescollector.entity.ActCategories;
import com.innopolis.innometrics.activitiescollector.repository.ActAppxCategoryRepository;
import com.innopolis.innometrics.activitiescollector.repository.ActCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {
    private final ActCategoryRepository actCategoryRepository;
    private final ActAppxCategoryRepository actAppxCategoryRepository;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public CategoryResponse createCategory(CategoryRequest categoryRequest, String userName) {
        ActCategories act = new ActCategories();
        act.setCatName(categoryRequest.getCategoryName());
        act.setIsActive("Y");
        act.setCreatedBy(userName);
        act = actCategoryRepository.save(act);
        return getCategoryResponse(act);
    }

    public CategoryResponse updateCategory(CategoryRequest categoryRequest, String userName) {
        ActCategories act;
        act = actCategoryRepository.getOne(categoryRequest.getCategoryId());
        act.setCatName(categoryRequest.getCategoryName());
        act.setIsActive(categoryRequest.getIsActive());
        act.setUpdateBy(userName);
        act.setLastUpdate(new Date());
        act = actCategoryRepository.save(act);
        return getCategoryResponse(act);
    }

    public CategoryListResponse getAllCategories() {
        List<ActCategories> categories;
        categories = actCategoryRepository.findAll();
        if (categories.isEmpty()) return null;
        CategoryListResponse response = new CategoryListResponse();
        ModelMapper modelMapper = new ModelMapper();
        for (ActCategories cat : categories) {
            CategoryResponse catDTO = modelMapper.map(cat, CategoryResponse.class);
            response.getCategories().add(catDTO);
        }
        return response;
    }

    public CategoryResponse getCategoryById(Integer categoryId) {
        List<ActCategories> act;
        act = actCategoryRepository.findAllById(Collections.singleton(categoryId));
        if (act.isEmpty()) return null;
        CategoryResponse response = new CategoryResponse();
        response.setCatId(act.get(0).getCatId());
        response.setCatName(act.get(0).getCatName());
        response.setIsActive(act.get(0).getIsActive());
        response.setCreatedBy(act.get(0).getCreatedBy());
        response.setCreationDate(act.get(0).getCreationDate());
        response.setUpdateBy(act.get(0).getUpdateBy());
        response.setLastUpdate(act.get(0).getLastUpdate());
        return response;
    }

    public AppCategoryResponse createAppCategory(AppCategoryRequest appcategoryRequest, String username) {
        ActAppxCategory app = new ActAppxCategory();
        app.setAppName(appcategoryRequest.getAppName());
        app.setCatId(appcategoryRequest.getCatId());
        app.setIsActive("Y");
        app.setCreatedBy(username);
        app.setCreationdate(new Date());
        return getAppCategoryResponse(app);
    }

    public AppCategoryResponse updateAppCategory(AppCategoryRequest appcategoryRequest, String userName) {
        ActAppxCategory app;
        app = actAppxCategoryRepository.getOne(appcategoryRequest.getAppId());
        app.setAppName(appcategoryRequest.getAppName());
        app.setCatId(appcategoryRequest.getCatId());
        app.setIsActive(appcategoryRequest.getIsActive());
        app.setUpdateBy(userName);
        app.setLastUpdate(new Date());
        return getAppCategoryResponse(app);
    }

    public AppCategoryResponse getAppCategoryById(Integer appid) {
        List<ActAppxCategory> app;
        app = actAppxCategoryRepository.findAllById(Collections.singleton(appid));
        AppCategoryResponse response = new AppCategoryResponse();
        if (app.isEmpty()) return null;
        response.setAppId(app.get(0).getAppid());
        response.setAppName(app.get(0).getAppName());
        response.setCatId(app.get(0).getCatId());
        response.setIsActive(app.get(0).getIsActive());
        response.setCreatedBy(app.get(0).getCreatedBy());
        response.setCreationDate(app.get(0).getCreationdate());
        response.setUpdateBy(app.get(0).getUpdateBy());
        response.setLastUpdate(app.get(0).getLastUpdate());
        return response;
    }

    public CategoriesTimeReportResponse getTimeReport(TimeReportRequest request) {
        List<ICategoriesReport> result = actCategoryRepository.getTimeReport(
                request.getProjectID(),
                request.getEmail(),
                (request.getMinDate() != null ? formatter.format(request.getMinDate()) : null),
                (request.getMaxDate() != null ? formatter.format(request.getMaxDate()) : null));
        CategoriesTimeReportResponse response = new CategoriesTimeReportResponse();
        for (ICategoriesReport a : result) {
            CategoriesReport temp = new CategoriesReport();
            temp.setCatName(a.getCatName());
            temp.setCatDescription(a.getCatDescription());
            temp.setTimeUsed(a.getTimeUsed());
            response.getReports().add(temp);
        }
        return response;
    }

    private CategoryResponse getCategoryResponse(ActCategories act) {
        CategoryResponse response = new CategoryResponse();
        response.setCatId(act.getCatId());
        response.setCatName(act.getCatName());
        response.setIsActive(act.getIsActive());
        response.setCreatedBy(act.getCreatedBy());
        response.setCreationDate(act.getCreationDate());
        response.setUpdateBy(act.getUpdateBy());
        response.setLastUpdate(act.getLastUpdate());
        return response;
    }

    private AppCategoryResponse getAppCategoryResponse(ActAppxCategory app) {
        app = actAppxCategoryRepository.save(app);
        AppCategoryResponse response = new AppCategoryResponse();
        response.setAppId(app.getAppid());
        response.setAppName(app.getAppName());
        response.setCatId(app.getCatId());
        response.setIsActive(app.getIsActive());
        response.setCreatedBy(app.getCreatedBy());
        response.setCreationDate(app.getCreationdate());
        response.setUpdateBy(app.getUpdateBy());
        response.setLastUpdate(app.getLastUpdate());
        return response;
    }
}
