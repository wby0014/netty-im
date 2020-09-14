package com.wby.netty.server.im.message;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Date 2020/9/7 19:22
 * @Author wuby31052
 */
public interface Message {

    String HEADER_PROTOCOL = "Message-Protocol";


    Map<String, List<String>> getHeaders();

    Object getContent();
}
