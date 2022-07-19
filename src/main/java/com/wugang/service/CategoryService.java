package com.wugang.service;

import com.wugang.pojo.Category;
import com.wugang.request.CategoryQueryRequest;
import com.wugang.request.CategorySaveRequest;
import com.wugang.response.CategoryQueryResponse;
import com.wugang.response.PageResponse;

import java.util.List;

public interface CategoryService {

    PageResponse<Category> queryList(CategoryQueryRequest categoryRequest);


    List<CategoryQueryResponse> queryAllList();

    void saveCategory(CategorySaveRequest categoryRequest);

    void deleteCategory(String categoryId);
}
