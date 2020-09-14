package com.wby.netty.server.im.protocal;

import com.wby.netty.server.im.message.Message;
import com.wby.netty.server.im.message.Request;
import org.springframework.beans.BeanUtils;

import java.util.Map;

/**
 * @Description json格式请求
 * @Date 2020/9/14 14:59
 * @Author wuby31052
 */
public class JsonRequest extends Request {

    public JsonRequest() {
    }

    public JsonRequest(Map<String, Object> dataset) {
        super.setHeader(Message.HEADER_PROTOCOL, "json");
        this.putHeader(dataset);
    }

    private void putHeader(Map<String, Object> dataset) {
        Object value;
        for (Map.Entry<String, Object> entry : dataset.entrySet()) {
            value = entry.getValue() == null ? "" : entry.getValue();
            if (BeanUtils.isSimpleValueType(value.getClass())) {
                super.setHeader(entry.getKey(), String.valueOf(value));
            } else if (BeanUtils.isSimpleProperty(value.getClass()) == false && value instanceof Map) {
                super.setContent(value);
            }
        }
    }

}
