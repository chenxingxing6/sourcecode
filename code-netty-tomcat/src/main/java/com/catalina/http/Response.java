package com.catalina.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:27
 * Desc:
 */
public class Response {
    private ChannelHandlerContext ctx;
    private HttpRequest req;
    private static Map<Integer,HttpResponseStatus> statusMapping = new HashMap<Integer, HttpResponseStatus>();
    static{
        statusMapping.put(200, HttpResponseStatus.OK);
        statusMapping.put(404, HttpResponseStatus.NOT_FOUND);
    }

    public Response(ChannelHandlerContext ctx, HttpRequest req) {
        this.ctx = ctx;
        this.req = req;
    }

    public void write(String text){
        write(text, 200);
    }

    public void write(String text, Integer status){
       try {
           if (text.isEmpty()){
               return;
           }
           FullHttpResponse response = new DefaultFullHttpResponse(
                   HttpVersion.HTTP_1_1,
                   statusMapping.get(status),
                   Unpooled.wrappedBuffer(text.getBytes("UTF-8"))
           );
           response.headers().set("Content-Type", "text/json");
           response.headers().set("Content-Length", response.content().readableBytes());
           response.headers().set("expires", 0);
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
