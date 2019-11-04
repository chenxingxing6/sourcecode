package com.demo.pubsub;

import com.alibaba.fastjson.JSON;
import com.demo.domain.Message;
import com.demo.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/11/4 15:32
 * Desc: 生产者
 */
@Component("pubsubProducer")
public class Producer {
    @Resource
    private RedisService redisService;

    public void producer(){
        for (int i = 1; i <= 100; i++) {
            String body = "{key:"+ i +"}";
            Message message = new Message("" +i, Constant.topic, "tag", "053a842", new Date(), body);
            redisService.publish(Constant.topic, JSON.toJSONString(message));
            System.out.println("发布消息: msgId：" + message.getMsgId() + "  msg:" + message.getMsgBody());
            try {
                TimeUnit.SECONDS.sleep(1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
