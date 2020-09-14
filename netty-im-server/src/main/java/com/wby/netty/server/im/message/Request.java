package com.wby.netty.server.im.message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Date 2020/9/7 19:23
 * @Author wuby31052
 */
public class Request implements Message {

    private Map<String, List<String>> headers = new HashMap<>();
    private Object content = null;

    public Request(Map<String, List<String>> headers, Object content) {
        this.headers = headers;
        this.content = content;
    }

    public Request() {
    }

    public void setHeader(String name, Object value) {
        headers.put(name, Arrays.asList(String.valueOf(value)));
    }

    public void setContent(Object content) {
        this.content = content;
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
