## MessagePack
MessagePack是一个高效的二进制序列化框架，它像JSON一样支持不同语言间的数据交换，但是它性能更快，序列化后码流更小

MessagePack特点：
> 1.编解码高效，性能高；   
2.序列化后的码流小；   
3.支持跨语言。   

---
## 聊天室实现原理
通过netty实现webSocket
> 1.实现解析http请求   
> 2.实现解析websocket请求

```java
package com.demo.server;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.log.level.Level;
import com.demo.protocol.IMDecoder;
import com.demo.protocol.IMEncoder;
import com.demo.server.handler.HttpHandler;
import com.demo.server.handler.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * User: lanxinghua
 * Date: 2019/10/10 14:58
 * Desc: 聊天服务端
 */
public class ChatServer {
    private static final Log logger = LogFactory.get();

    public static void main(String[] args) {
        logger.log(Level.DEBUG, "聊天服务端开启....");
        new ChatServer().start(8080);

    }

    public void start(int port){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 2.解析http请求
                            pipeline.addLast(new HttpServerCodec());
                            //主要是将同一个http请求或响应的多个消息对象变成一个 fullHttpRequest完整的消息对象
                            pipeline.addLast(new HttpObjectAggregator(64*1024));
                            //主要用于处理大数据流,比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的 ,加上这个handler我们就不用考虑这个问题了
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpHandler());

                            // 3.解析websocket请求
                            pipeline.addLast(new WebSocketServerProtocolHandler("/myim"));
                            pipeline.addLast(new IMEncoder());
                            pipeline.addLast(new WebSocketHandler());
                            pipeline.addLast(new IMDecoder());
                        }
                    });
            ChannelFuture f = bootstrap.bind(port).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

```
需要注意的是，怎么去编码和解码，前端发送过来的消息怎么映射成我们所需要的对象，在编码解码时，我们使用了MessagePack序列化框架。

---

