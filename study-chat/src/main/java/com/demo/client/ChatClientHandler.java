package com.demo.client;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.demo.protocol.IMMessage;
import com.demo.protocol.IMP;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Scanner;

/**
 * User: lanxinghua
 * Date: 2019/10/14 09:20
 * Desc:
 */
public class ChatClientHandler extends ChannelInboundHandlerAdapter {
    private static final Log log = LogFactory.get(ChatClientHandler.class);
    private String nickName;
    private ChannelHandlerContext ctx;

    public ChatClientHandler(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        IMMessage message = (IMMessage) msg;
        log.info("收到消息：" + message.toString());
    }

    private boolean sendMsg(IMMessage msg){
        ctx.channel().writeAndFlush(msg);
        log.info("已发送至聊天面板,请继续输入");
        return msg.getCmd().equals(IMP.LOGOUT.getName()) ? false : true;
    }


    private void session(){
        new Thread(() -> {
            System.out.println(nickName +"你好，请在控制台输入指令:");
            IMMessage msg = null;
            Scanner scanner = new Scanner(System.in);
            do{
                if (scanner.hasNext()){
                    String cmd = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(cmd)){
                        msg = new IMMessage(IMP.LOGOUT.getName(), System.currentTimeMillis(), nickName);
                    }else {
                        msg = new IMMessage(IMP.CHAT.getName(), System.currentTimeMillis(), nickName, cmd);
                    }
                }
            }while (sendMsg(msg));
        }).start();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
        IMMessage message = new IMMessage(IMP.LOGIN.getName(), System.currentTimeMillis(), nickName);
        sendMsg(message);
        log.info("登陆成功");
        session();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("与服务器断开连接:"+cause.getMessage());
        ctx.close();
    }

}
