package com.demo.server.handler;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.demo.processor.MsgProcessor;
import com.demo.protocol.IMMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * User: lanxinghua
 * Date: 2019/10/10 16:36
 * Desc:
 */
public class SocketHandler extends SimpleChannelInboundHandler<IMMessage> {
    private static Log LOG = LogFactory.get(SocketHandler.class);
    private MsgProcessor processor = new MsgProcessor();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMMessage msg) throws Exception {
        System.out.println("Socket进行read");
        LOG.info("Socket:read");
        processor.sendMsg(ctx.channel(), "[蓝星花]");
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        LOG.info("Socket创建...");
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel client = ctx.channel();
        processor.logout(client);
        LOG.info("Socket Client:" + processor.getNickName(client) + "离开");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("channelInactive");
        super.channelInactive(ctx);
    }
    /**
     * tcp链路建立成功后调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOG.info("Socket Client: 有客户端连接："+ processor.getAddress(ctx.channel()));
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.info("Socket Client: 与客户端断开连接:"+cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
