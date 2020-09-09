package com.wby.netty.server.im.server;

/**
 * @Description
 * @Date 2020/9/7 18:52
 * @Author wuby31052
 */
public interface IServerBootstrap {

    void startup() throws InterruptedException;

    void destroy();

}
