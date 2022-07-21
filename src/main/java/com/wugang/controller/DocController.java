package com.wugang.controller;

import com.wugang.pojo.Doc;
import com.wugang.request.DocQueryRequest;
import com.wugang.request.DocSaveRequest;
import com.wugang.response.DocQueryResponse;
import com.wugang.response.CommonResponse;
import com.wugang.response.PageResponse;
import com.wugang.service.impl.DocServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocServiceImpl docService;

    /**
     * 分页查询doc列表
     * @param docRequest
     * @return
     */
    @GetMapping("/list")
    public CommonResponse<PageResponse<Doc>> list(DocQueryRequest docRequest) {
        PageResponse<Doc> docs = docService.queryList(docRequest);
        return new CommonResponse(docs);
    }

    /**
     * 所有doc信息
     * @return
     */
    @GetMapping("/allList")
    public CommonResponse allList(DocQueryRequest docQueryRequest) {
        List<Doc> categories = docService.queryAllList(docQueryRequest);
        return new CommonResponse(categories);
    }

    /**
     * 保存doc编辑
     * @param docRequest
     * @return
     */
    @PostMapping("/saveDoc")
    public CommonResponse saveDoc(@RequestBody DocSaveRequest docRequest) {
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
