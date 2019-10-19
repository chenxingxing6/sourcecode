package com.demo.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/10/19 17:02
 * Desc:
 */
@Component
public class RedisLock {
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 加锁标志
     */
    public static final String LOCKED = "TRUE";

    /**
     * 默认超时时间（秒）
     */
    public static final int DEFAULT_TIME_OUT = 5;



    public static final Random RANDOM = new Random();

    public synchronized boolean lock(String key){
        return lock(key, DEFAULT_TIME_OUT);
    }

    public synchronized boolean lock(String key, int expire) {
        try{
            if(this.redisTemplate.opsForValue().setIfAbsent(key,LOCKED, expire, TimeUnit.SECONDS)){
                return true;
            }
            // 短暂休眠，避免出现活锁
            Thread.sleep(3, RANDOM.nextInt(500));
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Locking error",e);
        }
        return false;
    }

    public void unlock(String key){
        redisTemplate.delete(key);
    }
}
