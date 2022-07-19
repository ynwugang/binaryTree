package com.wugang.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wugang.mapper.CategoryMapper;
import com.wugang.pojo.Category;
import com.wugang.request.CategoryQueryRequest;
import com.wugang.request.CategorySaveRequest;
import com.wugang.response.PageResponse;
import com.wugang.service.CategoryService;
import com.wugang.util.CopyUtil;
import com.wugang.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询电子书列表
     * @param categoryRequest
     * @return
     */
    @Override
    public PageResponse<Category> queryList(CategoryQueryRequest categoryRequest) {
        //将categoryRequest的信息存入到category中
        Category category = CopyUtil.copy(categoryRequest, Category.class);
        //使用PageHelper分页插件实现分页查询
        PageHelper.startPage(categoryRequest.getPage(), categoryRequest.getSize());
        //执行查询
        List<Category> categoryList = categoryMapper.queryList(category);

        //获取查询情况
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOGGER.info("总条数：{}", pageInfo.getTotal());
        LOGGER.info("总页数：{}", pageInfo.getPages());

        //存储查询到的数据以及总条数
        PageResponse<Category> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(categoryList);

        return pageResponse;
    }

    /**
     * 保存电子书修改
     * @param categoryRequest
     * @return
     */
    @Override
    public void saveCategory(CategorySaveRequest categoryRequest) {
        Category category = CopyUtil.copy(categoryRequest, Category.class);

        if (ObjectUtils.isEmpty(categoryRequest.getId())){
            //新增
            category.setId(String.valueOf(snowFlake.nextId()));
            categoryMapper.insertCategory(category);
        } else {
            //更新
            categoryMapper.updateCategory(category);
        }
    }

    /**
     * 删除电子书
     * @param categoryId
     */
    @Override
    public void deleteCategory(String categoryId) {
        categoryMapper.deleteCategoryById(categoryId);
    }
}
