package com.demo;

import com.alibaba.fastjson.JSON;
import com.demo.redis.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.Serializable;
import java.util.Date;

/**
 * User: lanxinghua
 * Date: 2019/11/4 11:47
 * Desc: redis实现消息队列
 * 消息队列的先进先出(FIFO)的特点结合Redis的list中的push和pop操作进行封装
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TranscationApplication.class)
public class RedisQueueTest {
    public static byte[] redisKey = "key".getBytes();

    @Autowired
    private RedisService redisService;

    @Test
    public void test(){
        init();
        System.out.println("100万条消息生产完成.....");
        pop();
    }

    // 100万消息,消息队列的生产者
    private void init(){
        for (int i = 0; i < 1000000; i++) {
            String body = "{key:"+ i +"}";
            Message message = new Message("" +i, "topic", "tag", "053a842ce01e4c5d9295b064634a268b", new Date(), body);
            redisService.lpush(redisKey, JSON.toJSONBytes(message));
        }
    }

    // 100万消息,消息队列的消费者
    private void pop(){
        byte[] bytes = redisService.rpop(redisKey);
        Message message = JSON.parseObject(bytes, Message.class);
        if (message != null){
            System.out.println("msgId:" + message.getMsgId() + JSON.toJSONString(message));
        }
    }

    class Message implements Serializable{
        private String msgId;
        private String topic;
        private String tag;
        private String key;
        private Date storeTime;
        private String msgBody;

        public Message(String msgId, String topic, String tag, String key, Date storeTime, String msgBody) {
            this.msgId = msgId;
            this.topic = topic;
            this.tag = tag;
            this.key = key;
            this.storeTime = storeTime;
            this.msgBody = msgBody;
        }

        public String getMsgId() {
            return msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Date getStoreTime() {
            return storeTime;
        }

        public void setStoreTime(Date storeTime) {
            this.storeTime = storeTime;
        }

        public String getMsgBody() {
            return msgBody;
        }

        public void setMsgBody(String msgBody) {
            this.msgBody = msgBody;
        }
    }
}
