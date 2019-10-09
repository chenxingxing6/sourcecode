package com.catalina.server;

import com.catalina.config.Config;
import com.catalina.http.Request;
import com.catalina.http.Response;
import com.catalina.http.Servlet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:41
 * Desc:
 */
public class TomcatHandler extends ChannelInboundHandlerAdapter {
    private static final Map<Pattern,Class<?>> servletMapping = new HashMap<Pattern, Class<?>>();
    private static String applicationName;


    static {
        Config.load("web.properties");
        applicationName = Config.getValue("application.name");
        for (String key : Config.getKeys()) {
            if (key.startsWith("servlet")){
                String name = key.replaceFirst("servlet.", "");
                if (name.indexOf(".") != -1){
                    name = name.substring(0, name.indexOf("."));
                }
                String pattern = Config.getValue("servlet." + name + ".urlPattern").replaceAll("\\*", ".*");
                if (applicationName != null){
                    pattern = "/"+ applicationName + pattern;
                }
                String className = Config.getValue("servlet." + name + ".className");
                if (!servletMapping.containsKey(pattern)){
                    try {
                        servletMapping.put(Pattern.compile(pattern), Class.forName(className));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) msg;
            Request request = new Request(ctx, httpRequest);
            Response response = new Response(ctx, httpRequest);
            doServlet(request, response);
        }
    }

    public void doServlet(Request request, Response response){
        String uri = request.getUri();
        String requestType = request.getRequestType();
        System.out.println("请求：" + uri + "type:" + requestType);
        if (applicationName != null && !uri.contains(applicationName)){
            String out = String.format("404 Not Found");
            response.write(out);
            return;
        }
        try {
            boolean hasPattern = false;
            for (Map.Entry<Pattern, Class<?>> entry : servletMapping.entrySet()) {
                if (entry.getKey().matcher(uri).matches()){
                    Servlet servlet = (Servlet) entry.getValue().newInstance();
                    if ("get".equalsIgnoreCase(requestType)){
                        servlet.doGet(request, response);
                    }else {
                        servlet.doPost(request, response);
                    }
                    hasPattern = true;
                }
            }
            if (!hasPattern){
                String out = String.format("404 Not Found");
                response.write(out);
                return;
            }
        }catch (Exception e){
            String out = String.format("500 Error msg:%s", e.getStackTrace());
            response.write(out);
        }
    }
}
