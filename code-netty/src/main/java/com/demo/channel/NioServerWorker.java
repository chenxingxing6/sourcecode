package com.demo.channel;

import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

/**
 * User: lanxinghua
 * Date: 2019/10/17 09:42
 * Desc:
 */
public class NioServerWorker extends AbstractNioSelector implements IWorker {
    public NioServerWorker(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor, threadName, selectorRunnablePool);
    }

    public void registerNewChannelTask(SocketChannel socketChannel) {

    }
}
