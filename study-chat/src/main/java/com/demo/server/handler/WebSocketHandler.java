package com.demo.server.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.demo.processor.MsgProcessor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * User: lanxinghua
 * Date: 2019/10/10 16:11
 * Desc:
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Log log = LogFactory.get(WebSocketHandler.class);
    private MsgProcessor processor = new MsgProcessor();

    /**
     * 服务端读取客户端写入的信息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 前端js里面发送消息格式：[LOGIN][1570712034096][蓝星花]
        processor.sendMsg(ctx.channel(), msg.text());
    }

    /**
     * 每当从服务端收到新的客户端连接时
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("聊天室：服务端收到新的客户端连接时");
    }


    /**
     * 每当服务端收到客户端断开连接时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel client = ctx.channel();
        processor.logout(client);
        String nickName = processor.getNickName(client);
        if (StrUtil.isEmpty(nickName)){
            return;
        }
        log.info("聊天室：" + nickName + "离开");
    }

    /**
     * 服务端监听到客户端活动
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("聊天室：客户端有活动");
    }

    /**
     * 客户端和服务器端断开连接（服务端close，客户端close）
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel client = ctx.channel();
        String nickName = processor.getNickName(client);
        if (StrUtil.isEmpty(nickName)){
            return;
        }
        log.info("聊天室：" + nickName + "掉线");
    }


    /**
     * IO错误
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("聊天室：" + "异常" + cause.getMessage());
        ctx.close();
    }
}
