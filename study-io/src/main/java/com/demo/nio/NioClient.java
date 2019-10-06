package com.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: cxx
 * @Date: 2019/10/6 11:44
 */
public class NioClient {
    private final InetSocketAddress address = new InetSocketAddress("localhost", 8080);
    private Selector selector;
    private SocketChannel client;
    private final ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
    private final ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);

    public NioClient() {
        try {
            // 不管三七二十一，先把路修好，把关卡开放
            client = SocketChannel.open(address);
            client.configureBlocking(false);
            // 开门接客
            selector = Selector.open();
            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listener(){
        try {
           while (selector.select() > 0){
               Set<SelectionKey> keys = selector.selectedKeys();
               Iterator<SelectionKey> iterator = keys.iterator();
               while (iterator.hasNext()){
                   SelectionKey key = iterator.next();
                   iterator.remove();
                   if (key.isReadable()){
                       receive(key);
                   }
                   if (key.isWritable()){
                       send(key);
                   }
               }
           }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void receive(SelectionKey key){
        try {
            System.out.println("接收消息");
            SocketChannel socketChannel=(SocketChannel)key.channel();
            socketChannel.read(receiveBuffer);
            receiveBuffer.flip();
            String receiveData= Charset.forName("UTF-8").decode(receiveBuffer).toString();
            System.out.println("receive server message:"+receiveData);
            receiveBuffer.clear();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void send(SelectionKey key){
        try {
            System.out.println("发送消息");
            SocketChannel socketChannel=(SocketChannel)key.channel();
            synchronized(sendBuffer){
                sendBuffer.put("发送消息....".getBytes());
                sendBuffer.flip();
                while(sendBuffer.hasRemaining()){
                    socketChannel.write(sendBuffer);
                }
                sendBuffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        new NioClient().listener();
    }
}
