package com.demo.p2p;

import com.alibaba.fastjson.JSON;
import com.demo.domain.Message;
import com.demo.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * User: lanxinghua
 * Date: 2019/11/4 15:32
 * Desc: 发布者
 */
@Component
public class Producer {
    @Resource
    private RedisService redisService;

    // 1万消息,消息队列的生产者
    public void producer(){
        for (int i = 1; i <= Constant.msgSize; i++) {
            String body = "{key:"+ i +"}";
            Message message = new Message("" +i, "topic", "tag", "053a842", new Date(), body);
            redisService.lpush(Constant.redisKey, JSON.toJSONString(message));
        }
    }
}
