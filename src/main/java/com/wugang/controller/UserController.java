package com.wugang.controller;

import com.alibaba.fastjson.JSONObject;
import com.wugang.exception.ConditionException;
import com.wugang.exception.conditionExceptionCode;
import com.wugang.pojo.User;
import com.wugang.request.UserLoginRequest;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserResetPasswordRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.CommonResponse;
import com.wugang.response.PageResponse;
import com.wugang.response.UserLoginResponse;
import com.wugang.service.impl.UserServiceImpl;
import com.wugang.util.CopyUtil;
import com.wugang.util.RedisUtil;
import com.wugang.util.SnowFlake;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.Serializable;
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

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页查询电子书列表
     *
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
     *
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
     *
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
     *
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
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public CommonResponse login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
//        //执行登陆
//        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);

//        String token = String.valueOf(snowFlake.nextId());
//        LOGGER.info("输出单点登陆token：{}，并放入redis中", token);
//        userLoginResponse.setToken(token);
//        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userLoginResponse), 1800, TimeUnit.SECONDS);

        String loginName = userLoginRequest.getLoginName();
        String password = userLoginRequest.getPassword();

        //用户信息
        User user = userService.queryUserByLoginName(loginName);
        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            throw new ConditionException(conditionExceptionCode.LOGIN_USER_ERROR);
        }

        String token = String.valueOf(snowFlake.nextId());
        LOGGER.info("输出单点登陆token：{}，并放入redis中", token);
        UserLoginResponse userLoginResponse = CopyUtil.copy(user, UserLoginResponse.class);
        userLoginResponse.setToken(token);

        redisUtil.set(token, userLoginResponse, 1800);

        return new CommonResponse(userLoginResponse);
    }

    /**
     * 未登录
     *
     * @return
     */
    @GetMapping("/no-auth")
    public CommonResponse noAuth() {
        return CommonResponse.fail("未登录");
    }

    /**
     * 退出登陆
     *
     * @param token
     * @return
     */
    @GetMapping("/logout/{token}")
    public CommonResponse logout(@PathVariable String token) {
        LOGGER.info("从redis中删除token：{}", token);
        return CommonResponse.success();
    }
}
