package com.wby.netty.client.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description
 * @Date 2020/9/9 17:03
 * @Author wuby31052
 */
public class ImJsonClientDecoder extends SimpleChannelInboundHandler {

    private static final Logger logger = LoggerFactory.getLogger(ImJsonClientDecoder.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        logger.info("client decoder receive");
    }
}
