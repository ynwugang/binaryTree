package com.wugang.config;

import com.wugang.mapper.UserMapper;
import com.wugang.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

/**
 * 自定义realm，此处只需要认证，该系统无须授权，所以继承认证Realm即可
 */
public class UserRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    UserMapper userMapper;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        LOGGER.info("开始执行认证方法=>doGetAuthenticationInfo");

        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        //根据登录名查询用户信息
        User userDb = userMapper.queryUserByLoginName(token.getUsername());
        //用户名验证
        if (ObjectUtils.isEmpty(userDb)) {
            //用户不存在
            LOGGER.info("用户名不存在，{}", token.getUsername());
            //此处return null 会抛出一个UnknownAccountException异常
            return null;
        }

        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("loginUser", userDb);

        return new SimpleAuthenticationInfo(userDb, userDb.getPassword(), this.getClass().getName());
    }
}
