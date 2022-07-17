package com.wugang.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wugang.mapper.EbookMapper;
import com.wugang.pojo.Ebook;
import com.wugang.request.EbookQueryRequest;
import com.wugang.request.EbookSaveRequest;
import com.wugang.response.PageResponse;
import com.wugang.service.EbookService;
import com.wugang.util.CopyUtil;
import com.wugang.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookServiceImpl implements EbookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EbookServiceImpl.class);

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询电子书列表
     * @param ebookRequest
     * @return
     */
    @Override
    public PageResponse<Ebook> queryList(EbookQueryRequest ebookRequest) {
//        //校验ebookRequest信息
//        if (StringUtils.hasLength(ebookRequest.getName())){
//
//        }

        //将ebookRequest的信息存入到ebook中
        Ebook ebook = CopyUtil.copy(ebookRequest, Ebook.class);
        //使用PageHelper分页插件实现分页查询
        PageHelper.startPage(ebookRequest.getPage(), ebookRequest.getSize());
        //执行查询
        List<Ebook> ebookList = ebookMapper.queryList(ebook);

        //获取查询情况
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOGGER.info("总条数：{}", pageInfo.getTotal());
        LOGGER.info("总页数：{}", pageInfo.getPages());

        //存储查询到的数据以及总条数
        PageResponse<Ebook> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(ebookList);

        return pageResponse;
    }

    /**
     * 保存电子书修改
     * @param ebookRequest
     * @return
     */
    @Override
    public void saveEbook(EbookSaveRequest ebookRequest) {
        Ebook ebook = CopyUtil.copy(ebookRequest, Ebook.class);

        if (ObjectUtils.isEmpty(ebookRequest.getId())){
            //新增
            ebook.setId(String.valueOf(snowFlake.nextId()));
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebookMapper.insertEbook(ebook);
        } else {
            //更新
            ebookMapper.updateEbook(ebook);
        }
    }

    /**
     * 删除电子书
     * @param ebookId
     */
    @Override
    public void deleteEbook(String ebookId) {
        ebookMapper.deleteEbookById(ebookId);
    }
}
