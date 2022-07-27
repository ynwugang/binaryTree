package com.wugang.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wugang.exception.ConditionException;
import com.wugang.exception.conditionExceptionCode;
import com.wugang.mapper.UserMapper;
import com.wugang.pojo.User;
import com.wugang.request.UserLoginRequest;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserResetPasswordRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.PageResponse;
import com.wugang.response.UserLoginResponse;
import com.wugang.service.UserService;
import com.wugang.util.CopyUtil;
import com.wugang.util.SnowFlake;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 查询电子书列表
     *
     * @param userRequest
     * @return
     */
    @Override
    public PageResponse<User> queryList(UserQueryRequest userRequest) {
//        //校验userRequest信息
//        if (StringUtils.hasLength(userRequest.getName())){
//
//        }

        //将userRequest的信息存入到user中
        User user = CopyUtil.copy(userRequest, User.class);

        //校验分页参数
        if (userRequest.getPage() > 0 && userRequest.getSize() > 0) {
            //使用PageHelper分页插件实现分页查询
            PageHelper.startPage(userRequest.getPage(), userRequest.getSize());
        }


        //执行查询
        List<User> userList = userMapper.queryList(user);

        //获取查询情况
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOGGER.info("总条数：{}", pageInfo.getTotal());
        LOGGER.info("总页数：{}", pageInfo.getPages());

        //存储查询到的数据以及总条数
        PageResponse<User> pageResponse = new PageResponse<>();
        pageResponse.setTotal(pageInfo.getTotal());
        pageResponse.setList(userList);

        return pageResponse;
    }

    /**
     * 保存电子书修改
     *
     * @param userRequest
     * @return
     */
    @Override
    public void saveUser(UserSaveRequest userRequest) {
        User user = CopyUtil.copy(userRequest, User.class);

        //密码MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        if (ObjectUtils.isEmpty(userRequest.getId())) {
            User userDb = userMapper.queryUserByLoginName(user.getLoginName());
            if (ObjectUtils.isEmpty(userDb)) {
                //新增
                user.setId(String.valueOf(snowFlake.nextId()));
                userMapper.insertUser(user);
            } else {
                //用户名已存在
                throw new ConditionException(conditionExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        } else {
            //更新
            user.setPassword("");
            userMapper.updateUser(user);
        }
    }

    /**
     * 删除电子书
     *
     * @param userId
     */
    @Override
    public void deleteUser(String userId) {
        userMapper.deleteUserById(userId);
    }

    /**
     * 重置用户密码
     *
     * @param userResetPasswordRequest
     */
    @Override
    public void resetPassword(UserResetPasswordRequest userResetPasswordRequest) {
        User user = CopyUtil.copy(userResetPasswordRequest, User.class);

        //密码MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));

        userMapper.updateUser(user);
    }

    /**
     * 登陆
     *
     * @param userLoginRequest
     * @return
     */
    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        //获取当前用户
        Subject currentUser = SecurityUtils.getSubject();
        //将需要验证的用户名和密码传入UsernamePasswordToken对象
        UsernamePasswordToken token = new UsernamePasswordToken(userLoginRequest.getLoginName(), userLoginRequest.getPassword());
        //执行登陆
        try {
            currentUser.login(token);
        } catch (UnknownAccountException uae) {
            //用户不存在
            throw new ConditionException(conditionExceptionCode.LOGIN_USER_ERROR);
        } catch (IncorrectCredentialsException ice) {
            //密码错误
            throw new ConditionException(conditionExceptionCode.LOGIN_USER_ERROR);
        } catch (AuthenticationException ae) {
            //shiro登陆验证的最高异常
            throw new ConditionException(conditionExceptionCode.LOGIN_USER_ERROR);
        }

        //获取Session对象，这里的Session是shiro的Session，不是Http的Session
        Session session = currentUser.getSession();
        //获取用户信息，此处需要进行转换，存入session时候为POJO的User对象，返回给前端时需要转换为UserLoginResponse对象
        UserLoginResponse user = CopyUtil.copy(currentUser.getPrincipal(), UserLoginResponse.class);
        //将Token存入返回对象
        user.setToken(String.valueOf(session.getId()));
        //返回user
        return user;
    }

    /**
     * 根据loginName查询
     * @param loginName
     * @return
     */
    @Override
    public User queryUserByLoginName(String loginName) {
        return userMapper.queryUserByLoginName(loginName);
    }
}
