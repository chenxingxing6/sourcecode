package com.demo.client;

import com.demo.protocol.IMDecoder;
import com.demo.protocol.IMEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * User: lanxinghua
 * Date: 2019/10/14 09:19
 * Desc:
 */
public class ChatClient {
    private String host;
    private int port;

    public ChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(String nickName){
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel s) {
                            s.pipeline().addLast(new IMDecoder());
                            s.pipeline().addLast(new IMEncoder());
                            s.pipeline().addLast(new ChatClientHandler(nickName));
                        }
                    });
            ChannelFuture f = bootstrap.connect(host, port).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        // 实现跨平台，直接和web用户进行沟通
        new ChatClient("localhost", 8080).start("socketUser");
    }
}
