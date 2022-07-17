package com.wugang.controller;

import com.wugang.pojo.Ebook;
import com.wugang.request.EbookQueryRequest;
import com.wugang.request.EbookSaveRequest;
import com.wugang.response.CommonResponse;
import com.wugang.response.PageResponse;
import com.wugang.service.impl.EbookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookServiceImpl ebookService;

    /**
     * 分页查询电子书列表
     * @param ebookRequest
     * @return
     */
    @GetMapping("/list")
    public CommonResponse<PageResponse<Ebook>> list(EbookQueryRequest ebookRequest) {
        PageResponse<Ebook> ebooks = ebookService.queryList(ebookRequest);
        return new CommonResponse(ebooks);
    }

    /**
     * 保存电子书编辑
     * @param ebookRequest
     * @return
     */
    @PostMapping("/saveEbook")
    public CommonResponse saveEbook(@RequestBody EbookSaveRequest ebookRequest) {
        ebookService.saveEbook(ebookRequest);
        return CommonResponse.success();
    }

    /**
     * 删除电子书
     * @param ebookId
     * @return
     */
    @DeleteMapping("/deleteEbook/{id}")
    public CommonResponse deleteEbook(@PathVariable("id") String ebookId) {
        ebookService.deleteEbook(ebookId);
        return CommonResponse.success();
    }
}
