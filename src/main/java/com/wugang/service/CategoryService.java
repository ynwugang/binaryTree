package com.wugang.service;

import com.wugang.pojo.Category;
import com.wugang.request.CategoryQueryRequest;
import com.wugang.request.CategorySaveRequest;
import com.wugang.response.PageResponse;

public interface CategoryService {

    PageResponse<Category> queryList(CategoryQueryRequest categoryRequest);

    void saveCategory(CategorySaveRequest categoryRequest);

    void deleteCategory(String categoryId);
}
