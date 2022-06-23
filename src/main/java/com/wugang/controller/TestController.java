package com.wugang.controller;

import com.wugang.pojo.Test;
import com.wugang.service.TestService;
import com.wugang.service.impl.TestServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Resource
    private TestServiceImpl testService;

    @GetMapping("/hello")
    public String hello(){
        return "hello,word!123456";
    }

    @GetMapping("/query")
    public List<Test> queryList(){
        return testService.queryList();
    }
}
