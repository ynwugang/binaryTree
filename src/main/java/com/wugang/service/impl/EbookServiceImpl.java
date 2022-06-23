package com.wugang.service.impl;

import com.wugang.mapper.TestMapper;
import com.wugang.pojo.Test;
import com.wugang.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;

    @Override
    public List<Test> queryList() {
        return testMapper.queryList();
    }
}
