package com.wby.netty.server.im.handler;

import com.wby.netty.server.im.message.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description 空闲检测
 * @Date 2020/9/8 14:01
 * @Author wuby31052
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class HeartBeatServerHandler extends IdleStateHandler {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatServerHandler.class);

    private static final int READER_IDLE_TIME = 30;

    public HeartBeatServerHandler() {
        super(READER_IDLE_TIME, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg || !(msg instanceof Message)) {
            logger.info("heartbeat server channel read:" + msg);
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
        System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        super.channelIdle(ctx, evt);
    }

}
