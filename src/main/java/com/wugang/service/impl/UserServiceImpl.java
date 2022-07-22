package com.wugang.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wugang.exception.ConditionException;
import com.wugang.mapper.UserMapper;
import com.wugang.pojo.User;
import com.wugang.request.UserQueryRequest;
import com.wugang.request.UserSaveRequest;
import com.wugang.response.PageResponse;
import com.wugang.service.UserService;
import com.wugang.util.CopyUtil;
import com.wugang.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
        if (userRequest.getPage() > 0 && userRequest.getSize() > 0){
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
     * @param userRequest
     * @return
     */
    @Override
    public void saveUser(UserSaveRequest userRequest) {
        User user = CopyUtil.copy(userRequest, User.class);

        if (ObjectUtils.isEmpty(userRequest.getId())){
            List<User> users = userMapper.queryUserByLoginName(user.getLoginName());
            if (ObjectUtils.isEmpty(users)){
                //新增
                user.setId(String.valueOf(snowFlake.nextId()));
                userMapper.insertUser(user);
            } else {
                //用户名已存在
                throw new ConditionException("用户名已存在");
            }

        } else {
            //更新
            userMapper.updateUser(user);
        }
    }

    /**
     * 删除电子书
     * @param userId
     */
    @Override
    public void deleteUser(String userId) {
        userMapper.deleteUserById(userId);
    }
}
