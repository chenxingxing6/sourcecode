package com.demo.pubsub;

import com.demo.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/11/4 15:32
 * Desc: 消费者
 */
@Component("pubsubConsumer")
public class Consumer {
    @Resource
    private RedisService redisService;

    public void subscribe(){
        redisService.subscribe(new RedisMsgPubSubListener(), Constant.topic);
    }
}
