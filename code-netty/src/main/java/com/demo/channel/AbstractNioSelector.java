package com.demo.channel;

import java.nio.channels.Selector;
import java.util.concurrent.Executor;

/**
 * User: lanxinghua
 * Date: 2019/10/15 09:38
 * Desc: 抽象selector线程类
 */
public abstract class AbstractNioSelector implements Runnable {
    /**
     * 线程池
     */
    private final Executor executor = null;

    /**
     * 选择器
     */
    protected Selector selector;



    public void run() {

    }
}
