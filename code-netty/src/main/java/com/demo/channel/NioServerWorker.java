package com.demo.channel;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
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

    protected int select(Selector selector) throws Exception{
        return selector.select(500);
    }

    protected void process(Selector selector) throws Exception{
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        if (selectionKeys.isEmpty()){
            return;
        }
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        while (iterator.hasNext()){
            SelectionKey key = iterator.next();
            iterator.remove();
            // 获取到事件发送通道
            SocketChannel channel = (SocketChannel)key.channel();
            int len = 0;
            boolean failure = true;
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 读取数据
            try {
                len = channel.read(buffer);
                failure = false;
            }catch (Exception e){
                e.printStackTrace();
            }

            // 判断是否连接断开
            if (len <= 0 || failure){
                key.cancel();
                System.out.println("客户端断开连接....");
            }else {
                System.out.println(threadName +":收到数据：" + new String(buffer.array()));
                // 回写数据
                ByteBuffer outBuffer = ByteBuffer.wrap("收到".getBytes());
                channel.write(outBuffer);
            }
        }
    }

    public void registerNewChannelTask(final SocketChannel socketChannel) {
        final Selector selector = this.selector;
        addTask(new Runnable() {
            public void run() {
                try {
                    // 将客户端注册到selector
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
