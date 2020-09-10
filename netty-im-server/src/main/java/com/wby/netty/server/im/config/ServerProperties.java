package com.wby.netty.server.im.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2020/9/10 11:01
 * @Author wuby31052
 */
@Component
@PropertySource(value = "classpath:server.properties")
@ConfigurationProperties(prefix = "im.server")
public class ServerProperties {

    private String ip = "127.0.0.1";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
