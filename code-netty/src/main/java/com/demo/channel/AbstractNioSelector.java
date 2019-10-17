package com.demo.channel;

import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: lanxinghua
 * Date: 2019/10/15 09:38
 * Desc: 抽象selector线程类
 */
public abstract class AbstractNioSelector implements Runnable {
    /**
     * 线程池
     */
    private final Executor executor;

    /**
     * 选择器
     */
    protected Selector selector;

    /**
     * 选择器wakenUp状态标记
     */
    protected final AtomicBoolean wakenUp = new AtomicBoolean();

    /**
     * 任务队列
     */
    private final Queue<Runnable> taskQueue = new ConcurrentLinkedQueue<Runnable>();

    /**
     * 线程名
     */
    private String threadName;

    /**
     * 线程管理
     */
    protected NioSelectorRunnablePool selectorRunnablePool;

    public AbstractNioSelector(Executor executor, String threadName, NioSelectorRunnablePool selectorRunnablePool) {
        this.executor = executor;
        this.threadName = threadName;
        this.selectorRunnablePool = selectorRunnablePool;
        openSelector();
    }

    /**
     * 获取selector并启动线程
     */
    private void openSelector(){
        try {
            this.selector = Selector.open();
        }catch (Exception e){
            e.printStackTrace();
        }
        executor.execute(this);
    }

    public void run() {
        Thread.currentThread().setName(this.threadName);
        // selector轮询
        while (true){
            System.out.println(this.threadName + "轮询....");
            try {
                TimeUnit.SECONDS.sleep(1);
                wakenUp.set(false);
                select(this.selector);
                runTask();
                process(this.selector);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 注册任务，并激活selector
     * @param task
     */
    protected void addTask(Runnable task){
        taskQueue.add(task);
        Selector selector = this.selector;
        if (selector != null){
            if (wakenUp.compareAndSet(false, true)){
                selector.wakeup();
            }
        }else {
            taskQueue.remove(task);
        }
    }

    /**
     * 执行队列里面的任务
     */
    private void runTask(){
        // 自旋
        for (;;){
            Runnable task = taskQueue.poll();
            if (task == null){
                break;
            }
            task.run();
        }
    }

    protected abstract int select(Selector selector);

    /**
     * selector业务处理
     * @param selector
     */
    protected abstract void process(Selector selector);
}
