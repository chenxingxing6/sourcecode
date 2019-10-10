package com.demo.protocol;

import org.msgpack.annotation.Message;

/**
 * User: lanxinghua
 * Date: 2019/10/10 16:46
 * Desc: 自定义消息实体类
 */
@Message
public class IMMessage {
    // ip地址
    private String addr;

    // 命令类型 @IMP.name
    private String cmd;

    // 命令发送时间
    private long time;

    // 当前在线人数
    private int onlineCount;

    // 发送人
    private String sender;

    // 接收人
    private String receiver;

    // 消息内容
    private String msg;

    public IMMessage(){}


    public IMMessage(String cmd, long time, int onlineCount, String msg) {
        this.cmd = cmd;
        this.time = time;
        this.onlineCount = onlineCount;
        this.msg = msg;
    }

    public IMMessage(String cmd, long time, String sender){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
    }

    public IMMessage(String cmd, long time, String sender, String msg){
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.msg = msg;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
