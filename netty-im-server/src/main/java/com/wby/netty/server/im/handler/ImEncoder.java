package com.wby.netty.server.im.handler;

import com.alibaba.fastjson.JSON;
import com.wby.netty.server.im.message.Response;
import com.wby.netty.server.im.protocal.JsonResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @Description
 * @Date 2020/9/8 17:37
 * @Author wuby31052
 */
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class ImEncoder extends MessageToByteEncoder<Response> {

    private static final Logger logger = LoggerFactory.getLogger(ImEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext cxt, Response response, ByteBuf out) throws Exception {
        logger.info("response encode");
        String json = encodeJson(new JsonResponse(response));
        out.writeBytes(json.getBytes());
    }

    private String encodeJson(JsonResponse jsonResponse) {
        Map<String, Object> dataset = jsonResponse.getDataset();
        String heartBeat = (String) dataset.get("HeartBeat");
        if (StringUtils.hasLength(heartBeat)) {
            logger.info("返回心跳的报文是。。", heartBeat);
            heartBeat = heartBeat + "\r\n";
            return heartBeat;
        }
        String jsonStr = JSON.toJSONString(dataset);
        int jsonLength = jsonStr.getBytes().length;
        String length;
        if (jsonLength < 10000) {
            length = String.format("%04d", jsonLength);
        } else {
            length = String.valueOf(jsonLength);
        }
        String message = length + jsonStr;
        logger.info("返回的报文是。。", message);
        return message;
    }
}
