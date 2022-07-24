package com.wugang.controller;

import com.alibaba.fastjson.JSONObject;
import com.wugang.pojo.User;
import com.wugang.request.UserLoginRequest;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserResetPasswordRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.CommonResponse;
import com.wugang.response.PageResponse;
import com.wugang.response.UserLoginResponse;
import com.wugang.service.impl.UserServiceImpl;
import com.wugang.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

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
     * 重置密码
     * @param userResetPasswordRequest
     * @return
     */
    @PostMapping("/reset-password")
    public CommonResponse resetPassword(@Valid @RequestBody UserResetPasswordRequest userResetPasswordRequest) {
        userService.resetPassword(userResetPasswordRequest);
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

    /**
     * 登陆
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);

        String token = String.valueOf(snowFlake.nextId());
        LOGGER.info("输出单点登陆token：{}，并放入redis中", token);
        userLoginResponse.setToken(token);
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResponse), 1800, TimeUnit.SECONDS);

        return new CommonResponse(userLoginResponse);
    }
}
