package com.wugang.service;

import com.wugang.pojo.User;
import com.wugang.request.UserLoginRequest;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserResetPasswordRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.PageResponse;
import com.wugang.response.UserLoginResponse;

public interface UserService {

    PageResponse<User> queryList(UserQueryRequest userRequest);

    void saveUser(UserSaveRequest userRequest);

    void deleteUser(String userId);

    void resetPassword(UserResetPasswordRequest userResetPasswordRequest);

    UserLoginResponse login(UserLoginRequest userLoginRequest);

    /**
     * 根据loginName查询用户信息
     * @param loginName
     * @return
     */
    User queryUserByLoginName(String loginName);
}
