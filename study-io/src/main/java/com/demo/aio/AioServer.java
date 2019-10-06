package com.demo.aio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: cxx
 * @Date: 2019/10/6 12:49
 */
public class AioServer {
    private int port;

    public AioServer(int port) {
        this.port = port;
    }

    public void listener(){
        try {
            //线程缓冲池，为了体现异步
            ExecutorService executorService = Executors.newCachedThreadPool();
            //给线程池初始化一个线程
            AsynchronousChannelGroup threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            //同样的，也是先把高速公路修通
            final AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open(threadGroup);
            //打开高速公路的关卡
            server.bind(new InetSocketAddress(port));
            System.out.println("服务启动....");

            server.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                final ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

                @Override
                public void completed(AsynchronousSocketChannel result, Object attachment) {
                    try {
                        buffer.clear();
                        result.read(buffer).get();
                        buffer.flip();
                        String receive = Charset.forName("UTF-8").decode(buffer).toString();
                        System.out.println("接收消息：" + receive);
                        result.write(ByteBuffer.wrap("服务端已经接收到消息".getBytes()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        try {
                            result.close();
                            server.accept(null, this);
                        } catch (Exception e1) {
                            System.out.println(e1.toString());
                        }
                    }
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println("IO操作是失败: " + exc);
                }
            });

            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AioServer(8080).listener();
    }
}
