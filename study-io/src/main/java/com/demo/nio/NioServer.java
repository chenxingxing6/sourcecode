package com.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: cxx
 * @Date: 2019/10/6 11:44
 */
public class NioServer {
    private int port;
    private Selector selector;
    private ByteBuffer readBuffer = ByteBuffer.allocateDirect(1024);
    private ByteBuffer writeBuffer = ByteBuffer.allocateDirect(1024);

    public NioServer(int port) {
        try {
            this.port = port;
            // 1.先开路
            ServerSocketChannel channel = ServerSocketChannel.open();
            // 2.设置高速公路的关卡
            channel.bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            // 3.开门迎客
            selector = Selector.open();
            // 4.通知服务大厅工作人员可以接待了（这是个事件）
            channel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务端启动....");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void listener(){
        try {
            // 这里的循环可控，可控值是个固定的value
            while (true){
                // 在轮询，我们服务大厅中，到底有多少个人正在排队,会阻塞
                int wait = selector.select(1000);
                if (wait == 0){
                    System.out.println("没人排队....");
                    continue;
                }
                // 取号
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    // 处理完一个就赶出服务大厅
                    iterator.remove();
                    process(key);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void process(SelectionKey key){
        try {
            if (key.isAcceptable()) {
                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                SocketChannel client = server.accept();
                if (client != null){
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Accept Connection from " + client);
                }
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                //往缓冲区读数据
                readBuffer.clear();
                while (socketChannel.read(readBuffer) > 0){
                    readBuffer.flip();
                    String receiveData= Charset.forName("UTF-8").decode(readBuffer).toString();
                    System.out.println("receive client message:"+receiveData);
                }



                //key.interestOps(SelectionKey.OP_WRITE);
            }
            if (key.isWritable()) {
                SocketChannel socketChannel = (SocketChannel)key.channel();
                writeBuffer.clear();
                writeBuffer.put("服务器响应".getBytes());
                writeBuffer.flip();
                socketChannel.write(writeBuffer);
            }
        }catch (IOException e) {
            e.printStackTrace();
            key.cancel();
            if (key.channel() != null){
                try {
                    key.channel().close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        new NioServer(8080).listener();
    }
}
