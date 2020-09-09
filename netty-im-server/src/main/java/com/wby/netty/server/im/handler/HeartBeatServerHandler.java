package com.wby.netty.server.im.handler;

import com.wby.netty.server.im.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Description 空闲检测
 * @Date 2020/9/8 14:01
 * @Author wuby31052
 */
public class HeartBeatServerHandler extends IdleStateHandler {

    public HeartBeatServerHandler() {
        super(1, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg || !(msg instanceof Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        Message message = (Message) msg;
        if ("heartbeat".equals(message)) {
            if (ctx.channel().isActive()) {
                ctx.writeAndFlush(msg);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println("1秒内未读到数据，关闭连接");
        super.channelIdle(ctx, evt);
    }

}
