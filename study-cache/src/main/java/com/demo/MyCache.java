package com.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: cxx
 * @Date: 2019/11/3 17:42
 * 简单内存缓存实现
 */
public class MyCache implements ICache{
    private static Map<String, Entry> cache = new HashMap<String, Entry>();

    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public synchronized void put(String key, Object value) {
        this.put(key, value, 0L);
    }

    public synchronized void put(String key, Object value, Long secondExpire) {
        cache.remove(key);
        if (secondExpire > 0L){
            Future future = executor.schedule(() -> {
                synchronized (MyCache.class){
                    cache.remove(key);
                }
            }, secondExpire, TimeUnit.SECONDS);
            cache.put(key, new Entry(value, secondExpire, future));
        }else {
            cache.put(key, new Entry(value, null, null));
        }
    }

    public synchronized <T> T get(String key) {
        Entry entry = cache.get(key);
        if (entry != null && entry.isAlive()){
            return (T)entry.getObject();
        }
        return null;
    }

    public synchronized <T> T remove(String key) {
        Entry entry = cache.remove(key);
        if (entry == null){
            return null;
        }
        if (entry.getFuture() != null){
            entry.getFuture().cancel(true);
        }
        return entry.isAlive() ? (T) entry.getObject() : null;
    }

    public synchronized int size(){
        return cache.size();
    }


    class Entry{
        Object object;
        Long expireTime;
        Future future;

        public Entry(Object object, Long expireTime, Future future) {
            this.object = object;
            this.expireTime = (expireTime == null) ? null : expireTime*1000 + System.currentTimeMillis();
            this.future = future;
        }

        boolean isAlive(){
            return this.expireTime == null || this.expireTime >= System.currentTimeMillis();
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }

        public Long getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Long expireTime) {
            this.expireTime = expireTime;
        }

        public Future getFuture() {
            return future;
        }

        public void setFuture(Future future) {
            this.future = future;
        }
    }
}
