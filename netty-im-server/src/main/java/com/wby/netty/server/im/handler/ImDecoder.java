package com.wby.netty.server.im.handler;

import com.wby.netty.server.im.enums.IMP;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Date 2020/9/8 17:01
 * @Author wuby31052
 */
@Component
public class ImDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        byte[] array = new byte[length];
        String content = new String(array, in.readerIndex(), length);
        if (!(null == content) || "".equals(content.trim())) {
            if (!IMP.isIMP(content)) {
                ctx.channel().pipeline().remove(this);
                return;
            }
        }
        in.getBytes(in.readerIndex(), array, 0, length);
        out.add(null);
        in.clear();
    }

}
