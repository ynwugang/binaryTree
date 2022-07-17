package com.wugang.service;

import com.wugang.pojo.Ebook;
import com.wugang.request.EbookQueryRequest;
import com.wugang.request.EbookSaveRequest;
import com.wugang.response.PageResponse;

public interface EbookService {

    PageResponse<Ebook> queryList(EbookQueryRequest ebookRequest);

    void saveEbook(EbookSaveRequest ebookRequest);
}
