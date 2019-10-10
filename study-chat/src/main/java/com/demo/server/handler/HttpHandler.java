package com.demo.server.handler;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.level.Level;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;

/**
 * User: lanxinghua
 * Date: 2019/10/10 16:04
 * Desc: 处理http请求，访问网页
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Log logger = LogFactory.get(HttpHandler.class);
    private URL baseUrl = HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
    private final String webroot = "webroot";

    private File getResource(String fileName){
        try {
            String path = baseUrl.toURI() + webroot + "/" + fileName;
            path = path.contains("file:") ? path.substring(5) : path;
            path = path.replaceAll("//", "/");
            return new File(path);
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new RuntimeException("文件资源获取失败");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        String uri = request.uri();
        RandomAccessFile file = null;
        try {
            uri = uri.equals("/") ? "chat.html" : uri;
            file = new RandomAccessFile(getResource(uri), "r");
        }catch (Exception e){
            ctx.fireChannelRead(request.retain());
            return;
        }

        HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
        String contextType = "text/html;";
        if(uri.endsWith(".css")){
            contextType = "text/css;";
        }else if(uri.endsWith(".js")){
            contextType = "text/javascript;";
        }else if(uri.toLowerCase().matches("(jpg|png|gif)$")){
            String ext = uri.substring(uri.lastIndexOf("."));
            contextType = "image/" + ext;
        }
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, contextType + "charset=utf-8;");
        boolean keepAlive = HttpHeaders.isKeepAlive(request);
        if (keepAlive) {
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.write(response);
        ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
        ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
        file.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel client = ctx.channel();
        logger.log(Level.ERROR, "Client:" + client.remoteAddress() + "异常" + cause.getMessage());
        ctx.close();
    }
}
