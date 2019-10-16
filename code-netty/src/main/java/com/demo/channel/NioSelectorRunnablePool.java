package com.demo.channel;

import io.netty.util.NettyRuntime;

/**
 * User: lanxinghua
 * Date: 2019/10/15 09:41
 * Desc: selector线程管理者
 */
public class NioSelectorRunnablePool {
    public static void main(String[] args) {
        System.out.println(NettyRuntime.availableProcessors());
    }
}
