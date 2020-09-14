package com.wby.netty.server.im.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Date 2020/9/7 19:23
 * @Author wuby31052
 */
public class Response implements Message {

    private Map<String, List<String>> headers = new HashMap<>();
    private Object content = null;

    public Response(Message message) {
        this.headers = message.getHeaders();
        this.content = message.getContent();
    }

    public Response() {
    }

    public String getHeader(String name, String defaultHeader) {
        List<String> header = headers.get(name);
        if (header == null || headers.isEmpty()) {
            return defaultHeader;
        }
        return header.get(0);
    }

    public void addHeader(String name, Object value) {
        List values = headers.get(name);
        if (values == null) {
            values = new ArrayList<Object>();
        }
        values.add(value);
        headers.put(name, values);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Override
    public Object getContent() {
        return content;
    }
}
