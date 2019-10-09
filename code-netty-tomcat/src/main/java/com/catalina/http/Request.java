package com.catalina.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:27
 * Desc:
 */
public class Request {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public Request(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getUri(){
        return request.uri();
    }

    public String getRequestType(){
        return request.method().name();
    }

    public Map<String, List<String>> getParamters(){
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(getUri());
        return queryStringDecoder.parameters();
    }

    public String getParamter(String key){
        Map<String, List<String>> map = getParamters();
        List<String> params = map.get(key);
        if (params == null || params.isEmpty()){
            return null;
        }
        return params.get(0);
    }

}
