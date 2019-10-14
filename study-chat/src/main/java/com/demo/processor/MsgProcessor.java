package com.demo.processor;

import com.alibaba.fastjson.JSONObject;
import com.demo.protocol.IMDecoder;
import com.demo.protocol.IMEncoder;
import com.demo.protocol.IMMessage;
import com.demo.protocol.IMP;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * User: lanxinghua
 * Date: 2019/10/10 16:39
 * Desc:
 */
public class MsgProcessor {
    // 记录在线用户
    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 定义一下扩展属性
    private final AttributeKey<String> nickName = AttributeKey.valueOf("nickName");
    private final AttributeKey<String> ipAddr = AttributeKey.valueOf("ipAddr");
    private final AttributeKey<JSONObject> attrs = AttributeKey.valueOf("attrs");

    // 自定义解码器
    private IMDecoder decoder = new IMDecoder();
    // 自定义编码器
    private IMEncoder encoder = new IMEncoder();

    /**
     * 获取昵称
     */
    public String getNickName(Channel client){
        return client.attr(nickName).get();
    }

    /**
     * 获取用户ip地址
     */
    public String getAddress(Channel client){
        return client.remoteAddress().toString().replaceFirst("/","");
    }

    /**
     * 获取扩展属性
     */
    public JSONObject getAttrs(Channel client){
        try{
            return client.attr(attrs).get();
        }catch(Exception e){
            return null;
        }
    }

    private void setAttrs(Channel client,String key,Object value){
        try{
            JSONObject json = client.attr(attrs).get();
            json.put(key, value);
            client.attr(attrs).set(json);
        }catch(Exception e){
            JSONObject json = new JSONObject();
            json.put(key, value);
            client.attr(attrs).set(json);
        }
    }

    /**
     * 获取系统时间
     */
    private Long sysTime(){
        return System.currentTimeMillis();
    }

    /**
     * 登出通知
     */
    public void logout(Channel client){
        //如果nickName为null，没有遵从聊天协议的连接，表示未非法登录
        if(getNickName(client) == null){ return; }
        for (Channel channel : onlineUsers) {
            IMMessage msg = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickName(client) + "离开");
            // msg进行格式化
            String content = encoder.encode(msg);
            channel.writeAndFlush(new TextWebSocketFrame(content));
        }
        onlineUsers.remove(client);
    }



    public void sendMsg(Channel client,IMMessage msg){
        sendMsg(client, encoder.encode(msg));
    }

    /**
     * 发送消息
     * @param client
     * @param msg
     */
    public void sendMsg(Channel client,String msg){
        IMMessage request = decoder.decode(msg);
        if(null == request){ return; }

        String addr = getAddress(client);

        if(request.getCmd().equals(IMP.LOGIN.getName())){
            client.attr(nickName).getAndSet(request.getSender());
            client.attr(ipAddr).getAndSet(addr);
            onlineUsers.add(client);

            for (Channel channel : onlineUsers) {
                if(channel != client){
                    request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), getNickName(client) + "加入");
                }else{
                    request = new IMMessage(IMP.SYSTEM.getName(), sysTime(), onlineUsers.size(), "已与服务器建立连接！");
                }
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }else if(request.getCmd().equals(IMP.CHAT.getName())){
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                }else{
                    request.setSender(getNickName(client));
                }
                request.setTime(sysTime());
                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }else if(request.getCmd().equals(IMP.FLOWER.getName())){
            JSONObject attrs = getAttrs(client);
            long currTime = sysTime();
            if(null != attrs){
                long lastTime = attrs.getLongValue("lastFlowerTime");
                //60秒之内不允许重复刷鲜花
                int secends = 10;
                long sub = currTime - lastTime;
                if(sub < 1000 * secends){
                    request.setSender("you");
                    request.setCmd(IMP.SYSTEM.getName());
                    request.setMsg("您送鲜花太频繁," + (secends - Math.round(sub / 1000)) + "秒后再试");
                    String content = encoder.encode(request);
                    client.writeAndFlush(new TextWebSocketFrame(content));
                    return;
                }
            }

            //正常送花
            for (Channel channel : onlineUsers) {
                if (channel == client) {
                    request.setSender("you");
                    request.setMsg("你给大家送了一波鲜花雨");
                    setAttrs(client, "lastFlowerTime", currTime);
                }else{
                    request.setSender(getNickName(client));
                    request.setMsg(getNickName(client) + "送来一波鲜花雨");
                }
                request.setTime(sysTime());

                String content = encoder.encode(request);
                channel.writeAndFlush(new TextWebSocketFrame(content));
            }
        }
    }

}

