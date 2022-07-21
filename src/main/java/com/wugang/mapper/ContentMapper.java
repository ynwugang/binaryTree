package com.wugang.mapper;

import com.wugang.pojo.Content;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContentMapper {
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
     * 根据ID查询Content
     * @param id
     * @return
     */
    Content queryContentById(String id);
}
