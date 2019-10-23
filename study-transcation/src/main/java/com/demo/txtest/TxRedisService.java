package com.demo.txtest;

import com.demo.util.LockEnum;
import com.demo.util.LockUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * User: lanxinghua
 * Date: 2019/10/23 17:11
 * Desc: java中锁与@Transactional同时使用导致锁失效的问题
 * 问题分析：由于spring aop会在update方法之前开启事务，之后再加锁，当锁住代码后执行完后再提交事务：finally方法运行完，删除key后，事务还未提交。导致其他
 * 线程进行代码块，读取的数据不是最新的。
 *
 * 解决办法：在update之前就加上锁（还没开启事务前就加上锁）
 */
@Service
public class TxRedisService {
    @Autowired
    private LockUtil lockUtil;
    @Autowired
    private TxRedisService txRedisService;

    @Transactional
    public void update(String key){
        boolean lock = lockUtil.lock(LockEnum.TEST, key, false);
        if (!lock){
            throw new RuntimeException("当前人数过多，请稍后再试！");
        }
        try {
            System.out.println("处理业务逻辑: " + key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lockUtil.unlock(LockEnum.TEST, key);
        }
    }

    /**
     * 将锁粒度范围扩大
     * @param key
     */
    public void updatev1(String key){
        boolean lock = lockUtil.lock(LockEnum.TEST, key, false);
        if (!lock){
            throw new RuntimeException("当前人数过多，请稍后再试！");
        }
        try {
            txRedisService.innerUpdate(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lockUtil.unlock(LockEnum.TEST, key);
        }
    }

    @Transactional
    public void innerUpdate(String key){
        System.out.println("处理业务逻辑: " + key);
    }
}
