package com.wugang.service;

import com.wugang.pojo.Doc;
import com.wugang.request.DocQueryRequest;
import com.wugang.request.DocSaveRequest;
import com.wugang.response.DocQueryResponse;
import com.wugang.response.PageResponse;

import java.util.List;

public interface DocService {
    List<Doc> queryAllList(DocQueryRequest docQueryRequest);

    void saveDoc(DocSaveRequest docRequest);

    void deleteDoc(String docId);

    String queryContent(String id);
}
