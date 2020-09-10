package com.wby.netty.client.im.message;

/**
 * @Description
 * @Date 2020/9/7 19:49
 * @Author wuby31052
 */
public class ClientRequest implements Message {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
