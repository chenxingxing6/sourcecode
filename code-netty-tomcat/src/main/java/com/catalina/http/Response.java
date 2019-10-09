package com.catalina.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:27
 * Desc:
 */
public class Response {
    private ChannelHandlerContext ctx;
    private HttpRequest req;

    public Response(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public void write(String text){
       try {
           if (text.isEmpty()){
               return;
           }
           FullHttpResponse response = new DefaultFullHttpResponse(
                   HttpVersion.HTTP_1_1,
                   HttpResponseStatus.OK,
                   Unpooled.wrappedBuffer(text.getBytes("UTF-8"))
           );
           response.headers().set("Content-Type", "text/json");
           response.headers().set("Content-Length", response.content().readableBytes());
           response.headers().set("expires", 50000);
           response.headers().set("server", "my netty tomcat");
           ctx.write(response);
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           ctx.flush();
           ctx.close();
       }
    }
}
