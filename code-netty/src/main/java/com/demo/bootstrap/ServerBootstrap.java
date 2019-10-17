package com.demo.bootstrap;

import com.demo.channel.IBoss;
import com.demo.channel.NioSelectorRunnablePool;

import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * User: lanxinghua
 * Date: 2019/10/17 09:27
 * Desc:
 */
public class ServerBootstrap {
    private NioSelectorRunnablePool pool;

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
}
