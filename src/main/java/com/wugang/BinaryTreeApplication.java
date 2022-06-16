package com.wugang;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

@SpringBootApplication
public class BinaryTreeApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryTreeApplication.class);

    public static void main(String[] args) {
        ConfigurableEnvironment environment = SpringApplication.run(BinaryTreeApplication.class, args).getEnvironment();

        LOGGER.info("启动成功！");
        LOGGER.info("地址:\thttp://localhost:{}",environment.getProperty("server.port"));
    }

}
