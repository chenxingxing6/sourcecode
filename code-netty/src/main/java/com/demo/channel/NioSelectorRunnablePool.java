package com.demo.channel;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: lanxinghua
 * Date: 2019/10/15 09:41
 * Desc: selector线程管理者
 */
public class NioSelectorRunnablePool {
    // boss线程组
    private final AtomicInteger bossIndex = new AtomicInteger();
    private List<IBoss> bosses = new ArrayList<IBoss>();

    // worker线程组
    private final AtomicInteger workerIndex = new AtomicInteger();
    private List<IWorker> workers = new ArrayList<IWorker>();

    public NioSelectorRunnablePool(Executor worker) {
        initWorker(worker, Runtime.getRuntime().availableProcessors() * 2);
    }

    // 初始化
    public NioSelectorRunnablePool(Executor boss, Executor worker){
        initBoss(boss, 1);
        initWorker(worker, Runtime.getRuntime().availableProcessors() * 2);
    }

    private void initBoss(Executor boss, int count){
        for (int i = 0; i < count; i++) {
            bosses.add(new NioServerBoss(boss, "boss thread" + (i + 1), this));
        }
    }

    private void initWorker(Executor worker, int count){
        for (int i = 0; i < count; i++) {
            workers.add(new NioServerWorker(worker, "worker thread" + (i + 1), this));
        }
    }

    public IWorker nextWorker(){
        return workers.get(Math.abs(workerIndex.getAndIncrement() % workers.size()));
    }

    public IBoss nextBoss(){
        return bosses.get(Math.abs(bossIndex.getAndIncrement() % bosses.size()));
    }
}
