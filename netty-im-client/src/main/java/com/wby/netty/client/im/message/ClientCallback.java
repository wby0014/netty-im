package com.wby.netty.client.im.message;

/**
 * @Description
 * @Date 2020/9/7 19:51
 * @Author wuby31052
 */
public interface ClientCallback<T extends ClientResponse> {

    /**
     * 获取结果
     *
     * @return
     */
    T get();
}
