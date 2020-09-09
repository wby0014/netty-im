package com.wby.netty.server.im.config;

import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Description
 * @Date 2020/9/7 18:56
 * @Author wuby31052
 */
@Configuration
public class NettyConfig {

    private static final Logger log = LoggerFactory.getLogger(NettyConfig.class);

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        String bossCountStr = ConfigUtils.get("netty.boss.thread.count");
        int bossCount = 1;
        if (StringUtils.isEmpty(bossCountStr)) {
            log.debug("参数netty.boss.thread.count没有配置,使用默认值1");
        } else {
            bossCount = Integer.parseInt(bossCountStr);
        }
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workGroup() {
        String workCountStr = ConfigUtils.get("netty.work.thread.count");
        int workCount = 1;
        if (StringUtils.isEmpty(workCountStr)) {
            log.debug("参数netty.work.thread.count没有配置,使用默认值1");
        } else {
            workCount = Integer.parseInt(workCountStr);
        }
        return new NioEventLoopGroup(workCount);
    }
}
