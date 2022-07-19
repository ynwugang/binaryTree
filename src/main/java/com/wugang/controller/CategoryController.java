package com.wugang.controller;

import com.wugang.pojo.Category;
import com.wugang.request.CategoryQueryRequest;
import com.wugang.request.CategorySaveRequest;
import com.wugang.response.CategoryQueryResponse;
import com.wugang.response.CommonResponse;
import com.wugang.response.PageResponse;
import com.wugang.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 分页查询电子书列表
     * @param categoryRequest
     * @return
     */
    @GetMapping("/list")
    public CommonResponse<PageResponse<Category>> list(CategoryQueryRequest categoryRequest) {
        PageResponse<Category> categorys = categoryService.queryList(categoryRequest);
        return new CommonResponse(categorys);
    }

    /**
     * 所有分类信息
     * @return
     */
    @GetMapping("/allList")
    public CommonResponse allList() {
        List<CategoryQueryResponse> categories = categoryService.queryAllList();
        return new CommonResponse(categories);
    }

    /**
     * 保存电子书编辑
     * @param categoryRequest
     * @return
     */
    @PostMapping("/saveCategory")
    public CommonResponse saveCategory(@RequestBody CategorySaveRequest categoryRequest) {
        categoryService.saveCategory(categoryRequest);
        return CommonResponse.success();
    }

    /**
     * 删除电子书
     * @param categoryId
     * @return
     */
    @DeleteMapping("/deleteCategory/{id}")
    public CommonResponse deleteCategory(@PathVariable("id") String categoryId) {
        categoryService.deleteCategory(categoryId);
        return CommonResponse.success();
    }
}
