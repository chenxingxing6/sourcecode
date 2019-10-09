package com.catalina.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:30
 * Desc:
 */
public class Tomcat {

    /**
     * 开启服务
     * @param port
     * @throws Exception
     */
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 配置
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    // 主线程处理类
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // response编码
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            // request解码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            socketChannel.pipeline().addLast(new TomcatHandler());
                        }
                    })
                    // 对主线程，最大分配128个线程
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 子线程保存长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("Netty Tomcat Server Is Start...http://localhost:" + port);
            // 主线程wait
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
