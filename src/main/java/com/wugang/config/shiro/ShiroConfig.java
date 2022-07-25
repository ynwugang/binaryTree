package com.wugang.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    // shiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("webSecurityManager") DefaultWebSecurityManager webSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(webSecurityManager);

        //设置拦截
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/ebook/*", "authc");
        bean.setFilterChainDefinitionMap(filterMap);

        //
        bean.setLoginUrl("/no-auth");

        // 设置未授权页面
        bean.setUnauthorizedUrl("/no-auth");

        return bean;
    }


    /**
     * shiro 安全管理器，用于管理所有用户的安全操作
     * @param userRealm
     * @return
     */
    @Bean(name = "webSecurityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("getUserRealm") UserRealm userRealm){
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager();
        //关联自定义realm
        webSecurityManager.setRealm(userRealm);
        return webSecurityManager;
    }

    /**
     * 自定义realm
     * @return
     */
    @Bean
    public UserRealm getUserRealm(){
        UserRealm userRealm = new UserRealm();
        //设置加密方式
        userRealm.setCredentialsMatcher(new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME));
        return userRealm;
    }
}
