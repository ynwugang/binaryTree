package com.wugang.service;

import com.wugang.pojo.User;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.PageResponse;

public interface UserService {

    PageResponse<User> queryList(UserQueryRequest userRequest);

    void saveUser(UserSaveRequest userRequest);

    void deleteUser(String userId);
}
