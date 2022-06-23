package com.wugang.service.impl;

import com.wugang.mapper.EbookMapper;
import com.wugang.pojo.Ebook;
import com.wugang.pojo.EbookExample;
import com.wugang.request.EbookRequest;
import com.wugang.service.EbookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookServiceImpl implements EbookService {

    @Resource
    private EbookMapper ebookMapper;

    @Override
    public List<Ebook> queryList(Ebook ebook) {
        //校验ebook信息

        //执行查询
        return ebookMapper.queryList(ebook);
    }
}
