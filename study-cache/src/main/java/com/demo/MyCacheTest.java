package com.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: cxx
 * @Date: 2019/11/3 18:17
 */
public class MyCacheTest {
    public static void main(String[] args) throws Exception{
        MyCache cache = new MyCache();
        String key = "id";
        //不设置过期时间
        System.out.println("***********不设置过期时间**********");
        cache.put(key, 123);
        System.out.println("key:" + key + ", value:" + cache.get(key));
        System.out.println("key:" + key + ", value:" + cache.remove(key));
        System.out.println("key:" + key + ", value:" + cache.get(key));

        //设置过期时间
        System.out.println("\n***********设置过期时间**********");
        cache.put(key, "123456", 1L);
        System.out.println("key:" + key + ", value:" + cache.get(key));
        TimeUnit.SECONDS.sleep(1);
        System.out.println("key:" + key + ", value:" + cache.get(key));

        System.out.println("\n***********100w读写性能测试************");
        int threadSize = 10;
        ExecutorService pool = Executors.newFixedThreadPool(threadSize);
        int batchSize = 100000;
        {
            CountDownLatch latch = new CountDownLatch(threadSize);
            AtomicInteger atomicInteger = new AtomicInteger(0);
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < threadSize; i++) {
                pool.submit(() -> {
                    for (int i1 = 0; i1 < batchSize; i1++) {
                        int value = atomicInteger.incrementAndGet();
                        cache.put(key + value, value, 60L);
                    }
                    latch.countDown();
                });
            }
            latch.await();
            System.out.printf("添加耗时：%dms\n", System.currentTimeMillis() - startTime);
        }

        {
            CountDownLatch latch = new CountDownLatch(threadSize);
            AtomicInteger atomicInteger = new AtomicInteger(0);
            Long startTime = System.currentTimeMillis();
            for (int i = 0; i < threadSize; i++) {
                pool.submit(() -> {
                    for (int i1 = 0; i1 < batchSize; i1++) {
                        int value = atomicInteger.incrementAndGet();
                        int v = cache.get(key + value);
                        System.out.println(v);
                    }
                    latch.countDown();
                });
            }
            latch.await();
            System.out.printf("查询耗时：%dms\n", System.currentTimeMillis() - startTime);
        }

        System.out.println("当前缓存容量：" + cache.size());
    }
}
