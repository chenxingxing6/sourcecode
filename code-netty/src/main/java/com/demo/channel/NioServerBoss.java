package com.demo.channel;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
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

    protected int select(Selector selector) throws Exception{
        return selector.select();
    }

    // selector业务处理
    protected void process(Selector selector) throws Exception{
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()){
            return;
        }
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            iterator.remove();
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            // 新客户端
            SocketChannel channel = server.accept();
            // 设为非阻塞
            channel.configureBlocking(false);
            // 获取worker线程
            IWorker worker = selectorRunnablePool.nextWorker();
            // 注册新客户端接入任务
            worker.registerNewChannelTask(channel);
            System.out.println("新客户端连接......");
        }
    }

    public void registerAcceptChannelTask(final ServerSocketChannel serverSocketChannel) {
        final Selector selector = this.selector;
        addTask(new Runnable() {
            public void run() {
                try {
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
