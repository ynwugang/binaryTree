package com.wugang.controller;

import com.wugang.pojo.Ebook;
import com.wugang.request.EbookRequest;
import com.wugang.response.CommonResponse;
import com.wugang.service.impl.EbookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookServiceImpl ebookService;

    @GetMapping("/list")
    public CommonResponse<List<Ebook>> list( Ebook ebook) {
        List<Ebook> ebooks = ebookService.queryList(ebook);
        return new CommonResponse(ebooks);
    }
}
