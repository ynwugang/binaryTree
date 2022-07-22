package com.wugang.mapper;

import com.wugang.pojo.Doc;
import com.wugang.response.DocQueryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DocMapper {
    /**
     * 查询doc列表
     * @param doc
     * @return
     */
    List<Doc> queryList(Doc doc);

    /**
     * 更新doc
     * @param doc
     * @return
     */
    int updateDoc(Doc doc);

    /**
     * 新增Doc
     * @param doc
     * @return
     */
    int insertDoc(Doc doc);

    /**
     * 根据ID删除Doc
     * @param idList
     * @return
     */
    int deleteDocById(List<String> idList);

    /**
     * 获取所有Doc
     * @return
     */
    List<Doc> queryAllList(String ebookId);
}
