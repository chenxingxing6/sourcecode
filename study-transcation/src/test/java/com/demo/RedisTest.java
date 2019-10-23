package com.demo;

import com.demo.redis.RedisService;
import com.demo.txtest.TxRedisService;
import com.demo.util.LockEnum;
import com.demo.util.LockUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/10/19 16:45
 * Desc:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TranscationApplication.class)
public class RedisTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private LockUtil lockUtil;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TxRedisService txRedisService;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Test
    public void test(){
        stringRedisTemplate.opsForValue().set("key", "value",60*5, TimeUnit.SECONDS);//向redis里存入数据和设置缓存时间(5分钟)
        stringRedisTemplate.boundValueOps("key").increment(-1);//val做-1操作
        stringRedisTemplate.opsForValue().get("key");//根据key获取缓存中的val
        stringRedisTemplate.boundValueOps("key").increment(1);//val +1
        stringRedisTemplate.getExpire("key");//根据key获取过期时间
        stringRedisTemplate.getExpire("key",TimeUnit.SECONDS);//根据key获取过期时间并换算成指定单位
        stringRedisTemplate.delete("key");//根据key删除缓存
        stringRedisTemplate.hasKey("key");//检查key是否存在，返回boolean值
        stringRedisTemplate.opsForSet().add("key", "1","2","3");//向指定key中存放set集合
        stringRedisTemplate.expire("key",1000 , TimeUnit.MILLISECONDS);//设置过期时间
        stringRedisTemplate.opsForSet().isMember("key", "1");//根据key查看集合中是否存在指定数据
        stringRedisTemplate.opsForSet().members("key");//根据key获取set集合
    }

    @Test
    public void test1(){
        // stringRedisTemplate.opsForValue().set("aaaa", "value",5, TimeUnit.SECONDS);
        System.out.println(stringRedisTemplate.opsForValue().get("aaaa"));//根据key获取缓存中的val
    }

    @Test
    public void test2(){
        if(stringRedisTemplate.opsForValue().setIfAbsent("key", "1", 5, TimeUnit.SECONDS)){
            System.out.println("获取锁成功");
        }
        try {
            TimeUnit.SECONDS.sleep(4);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            stringRedisTemplate.delete("key");
            System.out.println("解锁成功");
        }
        System.out.println(stringRedisTemplate.opsForValue().get("key"));
    }


    @Test
    public void test03(){
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LockEnum lockEnum = LockEnum.TEST;
                    try {
                        lockUtil.lock(lockEnum, "mykey", true);
                        System.out.println("执行成功！");
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        lockUtil.unlock(lockEnum, "mykey");
                    }
                }
            }).start();
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * java中锁和事务同时使用，导致锁失效问题测试
     */
    @Test
    public void test04(){
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    txRedisService.update("lxh");
                }
            });
        }
        sleep();
    }

    @Test
    public void test05(){
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    txRedisService.updatev1("lxh");
                }
            });
        }
        sleep();
    }

    private void sleep(){
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){
            // ignore
        }
    }
}
