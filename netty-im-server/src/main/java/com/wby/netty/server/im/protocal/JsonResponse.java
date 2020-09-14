package com.wby.netty.server.im.protocal;

import com.wby.netty.server.im.message.Message;
import com.wby.netty.server.im.message.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Date 2020/9/14 15:37
 * @Author wuby31052
 */
public class JsonResponse extends Response {

    public JsonResponse(Message message) {
        super(message);
    }

    public Map<String, Object> getDataset() {
        Map<String, Object> map = new HashMap<String, Object>();
        for (String name : getHeaders().keySet()) {
            map.put(name, getHeader(name, null));
        }
        Object content = getContent();
        if (content instanceof Map) {
            map.put("json_body", content);
        }
        return map;
    }
}
