package com.wugang.controller;

import com.wugang.pojo.User;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.CommonResponse;
import com.wugang.response.PageResponse;
import com.wugang.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    /**
     * 分页查询电子书列表
     * @param userRequest
     * @return
     */
    @GetMapping("/list")
    public CommonResponse<PageResponse<User>> list(UserQueryRequest userRequest) {
        PageResponse<User> users = userService.queryList(userRequest);
        return new CommonResponse(users);
    }

    /**
     * 保存电子书编辑
     * @param userRequest
     * @return
     */
    @PostMapping("/saveUser")
    public CommonResponse saveUser(@Valid @RequestBody UserSaveRequest userRequest) {
        userService.saveUser(userRequest);
        return CommonResponse.success();
    }

    /**
     * 删除电子书
     * @param userId
     * @return
     */
    @DeleteMapping("/deleteUser/{id}")
    public CommonResponse deleteUser(@PathVariable("id") String userId) {
        userService.deleteUser(userId);
        return CommonResponse.success();
    }
}
