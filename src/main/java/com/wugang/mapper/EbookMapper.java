package com.wugang.mapper;

import com.wugang.pojo.Ebook;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EbookMapper {
    /**
     * 查询电子书列表
     * @param ebook
     * @return
     */
    List<Ebook> queryList(Ebook ebook);

    /**
     * 更新ebook
     * @param ebook
     * @return
     */
    int updateEbook(Ebook ebook);
}
