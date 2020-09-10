package com.wby.netty.client.im.handler;

import com.wby.netty.client.im.message.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Description 心跳包发送
 * @Date 2020/9/8 10:31
 * @Author wuby31052
 */
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HeartBeatClientHandler.class);

    public static final int HEARTBEAT_INTERVAL = 50;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // TODO: 2020/9/8 在Handler业务处理器被加入到流水线时，开始发送心跳数据包
        String heartMsg = "demo";
        heartBeat(ctx, heartMsg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("msg:" + msg);
        if (null == msg || !(msg instanceof Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        Message message = (Message) msg;
        if ("heartbeat".equals(message)) {
            logger.info("收到回写的HEART_BEAT消息from server");
            return;
        } else {
            super.channelRead(ctx, msg);
        }
    }

    public void heartBeat(ChannelHandlerContext ctx, Object msg) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                logger.info("发送HEART_BEAT消息to server");

                ctx.writeAndFlush(msg);
                // 递归调用，发送下一次的心跳
                heartBeat(ctx, msg);
            }
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
