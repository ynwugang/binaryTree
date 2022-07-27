package com.wugang.controller;

import com.wugang.pojo.Test;
import com.wugang.service.TestService;
import com.wugang.service.impl.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @Resource
    private TestServiceImpl testService;

    @Resource
    private RedisTemplate redisTemplate;

    @GetMapping("/hello")
    public String hello(){
        return "hello,word!123456";
    }

    @GetMapping("/query")
    public List<Test> queryList(){
        return testService.queryList();
    }

    @GetMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable String key, @PathVariable String value){
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOGGER.info("key: {}, value: {}", key, value);
        return "success";
    }

    @GetMapping("/redis/get/{key}")
    public Object get(@PathVariable String key){
        Object o = redisTemplate.opsForValue().get(key);
        LOGGER.info("key: {}, value: {}", key, o);
        return o;
    }
}
