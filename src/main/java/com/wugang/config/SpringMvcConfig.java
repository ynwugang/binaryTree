package com.wugang.config;

import com.wugang.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

//    //注入拦截器
//    @Resource
//    LogInterceptor logInterceptor;
//
//    /**
//     * 实现addInterceptors()方法，添加过滤器
//     * @param registry
//     */
//    public void addInterceptors(InterceptorRegistry registry){
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**");
//    }
}
