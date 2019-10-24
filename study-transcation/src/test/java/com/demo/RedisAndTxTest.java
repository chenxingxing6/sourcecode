package com.demo;

import com.demo.txtest.TxRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/10/24 09:49
 * Desc:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TranscationApplication.class)
public class RedisAndTxTest {
    @Autowired
    private TxRedisService txRedisService;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * java中锁和事务同时使用，导致锁失效问题测试
     */
    @Test
    public void test01(){
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

    /**
     * java中锁和事务同时使用，锁粒度扩大
     */
    @Test
    public void test02(){
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
        }
    }

}
