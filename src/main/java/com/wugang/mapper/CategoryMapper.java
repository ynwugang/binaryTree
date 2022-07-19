package com.wugang.mapper;

import com.wugang.pojo.Category;
import com.wugang.response.CategoryQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    /**
     * 查询category列表
     * @param category
     * @return
     */
    List<Category> queryList(Category category);

    /**
     * 更新category
     * @param category
     * @return
     */
    int updateCategory(Category category);

    /**
     * 新增Category
     * @param category
     * @return
     */
    int insertCategory(Category category);

    /**
     * 根据ID删除Category
     * @param id
     * @return
     */
    int deleteCategoryById(String id);

    /**
     * 获取所有Category
     * @return
     */
    List<CategoryQueryResponse> queryAllList();
}
