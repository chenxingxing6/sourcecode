package com.catalina.server;

import com.catalina.http.Request;
import com.catalina.http.Response;
import com.catalina.servlet.TestServlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:41
 * Desc:
 */
public class TomcatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) msg;
            Request request = new Request(ctx, httpRequest);
            Response response = new Response(ctx, httpRequest);
            doServlet(request, response);
        }
    }

    public void doServlet(Request request, Response resp){
        try {
            TestServlet.class.newInstance().doGet(request, resp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
