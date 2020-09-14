package com.wby.netty.server.im.handler;

import com.wby.netty.server.im.message.HeartBeatRequest;
import com.wby.netty.server.im.message.Request;
import com.wby.netty.server.im.message.Response;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2020/9/7 19:21
 * @Author wuby31052
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class NettyServletHandler extends SimpleChannelInboundHandler<Request> {

    private static final Logger log = LoggerFactory.getLogger(NettyServletHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        Response response = new Response();
        ChannelId channelId = ctx.channel().id();
        log.info("channelId: " + channelId);
        log.info("RequestMessage: " + request.toString());
        if (log.isDebugEnabled()) {
            log.debug(String.format("[%s] 接收到一个正确的请求", channelId));
        }
        if (request instanceof HeartBeatRequest) {
            // 心跳
            response.addHeader("HeartBeat", "OK");
            ctx.write(response);
        } else {
            // 业务接口
            // TODO: 2020/9/7 doSomething
            log.info("do something");
        }
        ChannelFuture future = ctx.writeAndFlush(response);
        future.addListener((ChannelFutureListener) future1 -> {
            // TODO: 2020/9/7 destroySomething
            if (future1.isSuccess()) {
                log.info("channel close");
//                future.channel().close();
            }
        });
        if (log.isDebugEnabled()) {
            log.debug(String.format("[%s] Servlet 响应发送完毕，本次 Servlet 请求处理结束！", channelId));
        }
    }


}
