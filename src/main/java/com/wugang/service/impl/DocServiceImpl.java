package com.wugang.service.impl;

import com.wugang.mapper.DocMapper;
import com.wugang.pojo.Doc;
import com.wugang.request.DocQueryRequest;
import com.wugang.request.DocSaveRequest;
import com.wugang.response.DocQueryResponse;
import com.wugang.response.PageResponse;
import com.wugang.service.DocService;
import com.wugang.util.CopyUtil;
import com.wugang.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class DocServiceImpl implements DocService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocServiceImpl.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询电子书列表
     * @param docRequest
     * @return
     */
    @Override
    @Deprecated
    public PageResponse<Doc> queryList(DocQueryRequest docRequest) {
//        //将docRequest的信息存入到doc中
//        Doc doc = CopyUtil.copy(docRequest, Doc.class);
//        //使用PageHelper分页插件实现分页查询
//        PageHelper.startPage(docRequest.getPage(), docRequest.getSize());
//        //执行查询
//        List<Doc> docList = docMapper.queryList(doc);
//
//        //获取查询情况
//        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
//        LOGGER.info("总条数：{}", pageInfo.getTotal());
//        LOGGER.info("总页数：{}", pageInfo.getPages());
//
//        //存储查询到的数据以及总条数
//        PageResponse<Doc> pageResponse = new PageResponse<>();
//        pageResponse.setTotal(pageInfo.getTotal());
//        pageResponse.setList(docList);
//
//        return pageResponse;

        return null;
    }

    /**
     * 查询所有分类信息
     * @return
     */
    @Override
    public List<Doc> queryAllList(DocQueryRequest docQueryRequest) {
        Doc doc = CopyUtil.copy(docQueryRequest, Doc.class);

        return docMapper.queryAllList(doc);
    }

    /**
     * 保存电子书修改
     * @param docRequest
     * @return
     */
    @Override
    public void saveDoc(DocSaveRequest docRequest) {
        Doc doc = CopyUtil.copy(docRequest, Doc.class);

        if (ObjectUtils.isEmpty(docRequest.getId())){
            //新增
            doc.setId(String.valueOf(snowFlake.nextId()));
            docMapper.insertDoc(doc);
        } else {
            //更新
            docMapper.updateDoc(doc);
        }
    }

    /**
     * 删除电子书
     * @param docIds
     */
    @Override
    public void deleteDoc(String docIds) {
        List<String> idList = Arrays.asList(docIds.split(","));


        docMapper.deleteDocById(idList);
    }
}
