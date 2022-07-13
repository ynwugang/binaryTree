package com.wugang.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LogFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //打印请求信息
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        LOGGER.info("------------LogFilter开始---------------");
        LOGGER.info("请求地址：{} {}", request.getRequestURL().toString(), request.getMethod());
        LOGGER.info("远程地址：{}", request.getRemoteAddr());

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        LOGGER.info("------------LogFilter结束，耗时：{} ms---------------", System.currentTimeMillis() - startTime);
    }
}
