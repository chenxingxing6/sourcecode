package com.demo;

import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * @Author: cxx
 * @Date: 2019/11/3 23:28
 */
public class JedisTest {
    public static void main(String[] args) throws Exception{
        Jedis jedis=new Jedis("localhost", 6379);
        String result = jedis.ping();
        System.out.println(result);

        TimeUnit.SECONDS.sleep(2);
        //关闭jedis
        jedis.close();
    }
}
