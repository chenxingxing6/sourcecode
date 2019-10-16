package com.demo.channel;

import java.nio.channels.ServerSocketChannel;

/**
 * User: lanxinghua
 * Date: 2019/10/15 09:34
 * Desc:
 */
public interface IBoss {
    /**
     * 加入一个新的serversockets
     * @param serverSocketChannel
     */
    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel);
}
