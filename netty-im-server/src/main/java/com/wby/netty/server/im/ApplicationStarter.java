package com.wby.netty.server.im;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description
 * @Date 2020/9/9 15:12
 * @Author wuby31052
 */
@SpringBootApplication(scanBasePackages = "com.wby.netty.server.im")
public class ApplicationStarter {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationStarter.class, args);
    }
}
