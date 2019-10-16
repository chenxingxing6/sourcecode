package com.demo.channel;

import java.nio.channels.SocketChannel;

/**
 * User: lanxinghua
 * Date: 2019/10/15 09:36
 * Desc:
 */
public interface IWorker {
    /**
     * 加入新的客户端会话
     * @param socketChannel
     */
    public void registerNewChannelTask(SocketChannel socketChannel);
}
