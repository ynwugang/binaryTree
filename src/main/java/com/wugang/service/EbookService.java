package com.wugang.service;

import com.wugang.pojo.Ebook;
import com.wugang.request.EbookRequest;

import java.util.List;

public interface EbookService {

    List<Ebook> queryList(Ebook ebook);
}
