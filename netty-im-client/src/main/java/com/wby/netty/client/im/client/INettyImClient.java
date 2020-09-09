package com.wby.netty.client.im.client;

import com.wby.netty.client.im.message.ClientCallback;
import com.wby.netty.client.im.message.ClientRequest;
import com.wby.netty.client.im.message.ClientResponse;

/**
 * @Description
 * @Date 2020/9/7 19:48
 * @Author wuby31052
 */
public interface INettyImClient {

    void startup();

    void shutdown();

    <req extends ClientRequest, resp extends ClientResponse> resp send(req request);

    void sendAysnc(ClientRequest request, ClientCallback callback);
}
