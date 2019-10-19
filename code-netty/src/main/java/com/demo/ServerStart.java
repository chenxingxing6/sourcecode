package com.demo;

import com.demo.bootstrap.ServerBootstrap;
import com.demo.channel.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * User: lanxinghua
 * Date: 2019/10/17 09:48
 * Desc:
 */
public class ServerStart {
    public static void main(String[] args) {
        // 初始化线程
        NioSelectorRunnablePool pool = new NioSelectorRunnablePool(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        // 配置
        ServerBootstrap bootstrap = new ServerBootstrap(pool);
        // 绑定端口
        bootstrap.bind(new InetSocketAddress(8888));
        System.out.println("server start......");
    }
}
