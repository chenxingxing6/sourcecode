package com.demo.protocol;

import org.msgpack.annotation.Message;

/**
 * User: lanxinghua
 * Date: 2019/10/10 16:55
 * Desc: instant messaging protocol即时通讯协议
 */
public enum IMP {
    SYSTEM("SYSTEM", "系统消息"),
    LOGIN("LOGIN", "登陆"),
    LOGOUT("LOGOUT", "登出"),
    CHAT("CHAT", "聊天"),
    FLOWER("FLOWER", "送鲜花")
    ;

    private String name;
    private String desc;

    IMP(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static IMP getInstance(String name){
        for (IMP value : IMP.values()) {
            if (value.getName().equalsIgnoreCase(name)){
                return value;
            }
        }
        return null;
    }

    public static boolean isIMP(String content){
        return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT)\\]");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
