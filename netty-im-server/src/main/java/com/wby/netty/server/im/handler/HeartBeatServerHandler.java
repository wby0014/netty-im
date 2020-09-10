package com.wby.netty.server.im.handler;

import com.wby.netty.server.im.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description 空闲检测
 * @Date 2020/9/8 14:01
 * @Author wuby31052
 */
@Component
public class HeartBeatServerHandler extends IdleStateHandler {

    private static final Logger logger = LoggerFactory.getLogger(HeartBeatServerHandler.class);

    public HeartBeatServerHandler() {
        super(3, 3, 3, TimeUnit.MINUTES);
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
        System.out.println("3分钟内未读到数据，关闭连接");
        super.channelIdle(ctx, evt);
    }

}
