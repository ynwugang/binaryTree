package com.wugang.service.impl;

import com.wugang.mapper.ContentMapper;
import com.wugang.mapper.DocMapper;
import com.wugang.pojo.Content;
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
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

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
        Content content = CopyUtil.copy(docRequest, Content.class);

        if (ObjectUtils.isEmpty(docRequest.getId())){
            //新增
            doc.setId(String.valueOf(snowFlake.nextId()));
            docMapper.insertDoc(doc);

            content.setId(doc.getId());
            contentMapper.insertContent(content);
        } else {
            //更新
            docMapper.updateDoc(doc);
            int count = contentMapper.updateContent(content);
            if (count == 0){
                content.setId(doc.getId());
                contentMapper.insertContent(content);
            }
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

    /**
     * 查询文档内容
     * @param id
     * @return
     */
    @Override
    public String queryContent(String id) {
        Content content = contentMapper.queryContentById(id);

        return ObjectUtils.isEmpty(content)? "" : content.getContent();
    }
}
