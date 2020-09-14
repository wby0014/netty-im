package com.wby.netty.server.im.handler;

import com.alibaba.fastjson.JSON;
import com.wby.netty.server.im.message.HeartBeatRequest;
import com.wby.netty.server.im.protocal.JsonRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Date 2020/9/8 17:01
 * @Author wuby31052
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class ImDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ImDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("ImDecoder receive msg");
        out.add(decodeInternal(ctx, in));
    }

    private Object decodeInternal(ChannelHandlerContext ctx, ByteBuf in) {
        int length = in.readableBytes();
        byte[] array = new byte[length];
        String content = new String(array, in.readerIndex(), length);
        if (!(null == content) || "".equals(content.trim())) {
            if ("ping".equals(content.trim())) {
                logger.info("这是一个心跳请求！", ctx.channel().id());
                return new HeartBeatRequest();
            } else {
                Map<String, Object> dataset = new HashMap<>(16);
                dataset.put("sessionKey", System.currentTimeMillis());
                dataset.putAll(JSON.parseObject(content.substring(content.length()), Map.class));
                return new JsonRequest(dataset);
            }
        }
        in.getBytes(in.readerIndex(), array, 0, length);
        in.clear();
        return new JsonRequest();
    }

}
