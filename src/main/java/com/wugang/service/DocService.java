package com.wugang.service;

import com.wugang.pojo.Doc;
import com.wugang.request.DocSaveRequest;

import java.util.List;

public interface DocService {
    List<Doc> queryAllList(String ebookId);

    void saveDoc(DocSaveRequest docRequest);

    void deleteDoc(String docId);

    String queryContent(String id);
}
