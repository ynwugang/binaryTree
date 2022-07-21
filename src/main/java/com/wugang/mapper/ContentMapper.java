package com.wugang.mapper;

import com.wugang.pojo.Content;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContentMapper {
    /**
     * 查询content列表
     * @param content
     * @return
     */
    List<Content> queryList(Content content);

    /**
     * 更新content
     * @param content
     * @return
     */
    int updateContent(Content content);

    /**
     * 新增Content
     * @param content
     * @return
     */
    int insertContent(Content content);

    /**
     * 根据ID删除Content
     * @param idList
     * @return
     */
    int deleteContentById(List<String> idList);

    /**
     * 获取所有Content
     * @return
     */
    List<Content> queryAllList(Content content);
}
