package com.wugang.mapper;

import com.wugang.pojo.Ebook;
import com.wugang.request.EbookRequest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface EbookMapper {
    List<Ebook> queryList(Ebook ebook);
}
