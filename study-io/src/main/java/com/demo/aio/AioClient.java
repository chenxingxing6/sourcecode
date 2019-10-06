package com.demo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Author: cxx
 * @Date: 2019/10/6 12:49
 */
public class AioClient {
    private AsynchronousSocketChannel channel;
    private String host;
    private int port;

    public AioClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            channel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listener(){
        // 这里只做写操作
        channel.connect(new InetSocketAddress(host, port), null, new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                try {
                    channel.write(ByteBuffer.wrap("测试....发送消息....".getBytes())).get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });

        // 读操作
        final ByteBuffer readBuf = ByteBuffer.allocate(1024);
        channel.read(readBuf, null, new CompletionHandler<Integer, Object>() {
            @Override
            public void completed(Integer result, Object attachment) {
                System.out.println("服务端响应结果：" + new String(readBuf.array()));
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                exc.printStackTrace();
            }
        });
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

    public static void main(String[] args) {
        new AioClient("localhost", 8080).listener();
    }
}
