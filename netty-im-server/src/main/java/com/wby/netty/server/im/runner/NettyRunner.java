package com.wby.netty.server.im.runner;

import com.wby.netty.server.im.server.ImNettyServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description
 * @Date 2020/9/9 15:37
 * @Author wuby31052
 */
@Component
public class NettyRunner implements CommandLineRunner {

    @Resource
    ImNettyServer imNettyServer;

    @Override
    public void run(String... args) throws Exception {
        imNettyServer.startup();
    }
}
