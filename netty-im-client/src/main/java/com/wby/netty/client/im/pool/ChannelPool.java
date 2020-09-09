package com.wby.netty.client.im.pool;

import io.netty.channel.Channel;

/**
 * @Description
 * @Date 2020/9/7 20:02
 * @Author wuby31052
 */
public interface ChannelPool {

    Channel borrowChannel() throws Exception;

    void returnChannel(Channel channel) throws Exception;
}
