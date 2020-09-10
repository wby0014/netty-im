package com.wby.netty.server.im.message;

/**
 * @Description
 * @Date 2020/9/7 19:28
 * @Author wuby31052
 */
public class HeartBeatRequest extends Request {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
