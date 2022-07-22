package com.wugang.mapper;

import com.wugang.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    /**
     * 查询user列表
     * @param user
     * @return
     */
    List<User> queryList(User user);

    /**
     * 根据loginName查询用户信息
     * @param loginName
     * @return
     */
    List<User> queryUserByLoginName(String loginName);

    /**
     * 更新user
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 新增User
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 根据ID删除User
     * @param id
     * @return
     */
    int deleteUserById(String id);
}
