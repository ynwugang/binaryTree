package com.wugang.service;

import com.wugang.pojo.Ebook;
import com.wugang.request.EbookRequest;
import com.wugang.response.PageResponse;

import java.util.List;

public interface EbookService {

    PageResponse<Ebook> queryList(EbookRequest ebookRequest);
}
