package com.wby.netty.server.im.enums;

/**
 * @Description
 * @Date 2020/9/8 16:53
 * @Author wuby31052
 */
public enum IMP {

    /**
     * 系统消息
     */
    SYSTEM("SYSTEM"),
    /**
     * 登录指令
     */
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    CHAT("CHAT"),
    FLOWER("FLOWER")
    ;

    private String name;

    IMP(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static boolean isIMP(String content) {
        return content.matches("^\\[(SYSTEM|LOGIN|LOGOUT|CHAT)\\]");
    }
}
