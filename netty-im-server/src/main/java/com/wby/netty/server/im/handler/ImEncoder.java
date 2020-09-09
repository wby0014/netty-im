package com.wby.netty.server.im.handler;

import com.wby.netty.server.im.message.IMMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Date 2020/9/8 17:37
 * @Author wuby31052
 */
@Component
public class ImEncoder extends MessageToByteEncoder<IMMessage> {


    @Override
    protected void encode(ChannelHandlerContext cxt, IMMessage msg, ByteBuf out) throws Exception {
        out.writableBytes();
    }
}
