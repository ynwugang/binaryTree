package com.wugang.controller;

import com.wugang.pojo.Doc;
import com.wugang.request.DocSaveRequest;
import com.wugang.response.CommonResponse;
import com.wugang.service.impl.DocServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocServiceImpl docService;

    /**
     * 所有doc信息
     * @return
     */
    @GetMapping("/allList/{id}")
    public CommonResponse allList(@PathVariable("id") String ebookId) {
        List<Doc> categories = docService.queryAllList(ebookId);
        return new CommonResponse(categories);
    }

    /**
     * 获取文档内容
     * @return
     */
    @GetMapping("/content/{id}")
    public CommonResponse content(@PathVariable String id) {
        String content = docService.queryContent(id);
        return new CommonResponse(content);
    }

    /**
     * 保存doc编辑
     * @param docRequest
     * @return
     */
    @PostMapping("/saveDoc")
    public CommonResponse saveDoc(@Valid @RequestBody DocSaveRequest docRequest) {
        docService.saveDoc(docRequest);
        return CommonResponse.success();
    }

    /**
     * 删除doc
     * @param docIds
     * @return
     */
    @DeleteMapping("/deleteDoc/{ids}")
    public CommonResponse deleteDoc(@PathVariable("ids") String docIds) {
        docService.deleteDoc(docIds);
        return CommonResponse.success();
    }
}
