package com.demo.channel;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executor;

/**
 * User: lanxinghua
 * Date: 2019/10/17 09:38
 * Desc:
 */
public class NioServerBoss extends AbstractNioSelector implements IBoss {

    public NioServerBoss(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        super(executor, threadName, selectorRunnablePool);
    }

    protected int select(Selector selector) {
        try {
            return selector.select();
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    protected void process(Selector selector) {

    }

    public void registerAcceptChannelTask(ServerSocketChannel serverSocketChannel) {

    }
}
