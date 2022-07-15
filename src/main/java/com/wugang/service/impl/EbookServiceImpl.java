package com.wugang.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wugang.mapper.EbookMapper;
import com.wugang.pojo.Ebook;
import com.wugang.pojo.EbookExample;
import com.wugang.request.EbookRequest;
import com.wugang.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookServiceImpl implements EbookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EbookServiceImpl.class);

    @Resource
    private EbookMapper ebookMapper;

    @Override
    public List<Ebook> queryList(Ebook ebook) {
        //校验ebook信息

        //使用PageHelper分页插件实现分页查询
        PageHelper.startPage(1,2);
        //执行查询
        List<Ebook> ebookList = ebookMapper.queryList(ebook);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);

        LOGGER.info("总条数：{}", pageInfo.getTotal());
        LOGGER.info("总页数：{}", pageInfo.getPages());

        return ebookList;
    }
}
