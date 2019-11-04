package com.demo.p2p;

import com.alibaba.fastjson.JSON;
import com.demo.domain.Message;
import com.demo.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/11/4 15:32
 * Desc: 消费者
 */
@Component
public class Consumer {
    @Resource
    private RedisService redisService;

    // 1万消息,消息队列的消费者
    public void consumer(){
        for (int i = 0; i < Constant.msgSize; i++) {
            String str = redisService.rpop(Constant.redisKey);
            Message message = JSON.parseObject(str, Message.class);
            if (message != null){
                System.out.println("消费成功：msgId:" + message.getMsgId() + JSON.toJSONString(message));
            }
        }
    }
}
