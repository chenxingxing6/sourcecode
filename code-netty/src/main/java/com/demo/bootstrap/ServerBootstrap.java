package com.demo.bootstrap;

import com.demo.channel.IBoss;
import com.demo.channel.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * User: lanxinghua
 * Date: 2019/10/17 09:27
 * Desc:
 */
public class ServerBootstrap {
    private NioSelectorRunnablePool pool;
    private final ByteBuffer sendBuffer=ByteBuffer.allocate(1024);
    private final ByteBuffer receiveBuffer=ByteBuffer.allocate(1024);

    public ServerBootstrap(NioSelectorRunnablePool pool) {
        this.pool = pool;
    }

    public void bind(SocketAddress address){
        try {
            // 获取一个ServerSocketChannel通道
            ServerSocketChannel serverChannel = ServerSocketChannel.open();
            // 设置通道非阻塞
            serverChannel.configureBlocking(false);
            // 将通道绑定到port端口
            serverChannel.socket().bind(address);

            // 获取boss线程
            IBoss boss = pool.nextBoss();
            // 向boss线程注册channel
            boss.registerAcceptChannelTask(serverChannel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void connect(String inetHost, int inetPort){
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress(inetHost, inetPort));
            client.configureBlocking(false);
            // 开门接客
            Selector selector = Selector.open();
            client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
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
            SocketChannel socketChannel=(SocketChannel)key.channel();
            synchronized(sendBuffer){
                sendBuffer.put("发送消息  ".getBytes());
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
}
