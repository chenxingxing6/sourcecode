import com.alibaba.fastjson.JSON;
import com.demo.RedisApplication;
import com.demo.redis.RedisService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;

/**
 * User: lanxinghua
 * Date: 2019/11/4 11:47
 * Desc: redis实现消息队列
 * 消息队列的先进先出(FIFO)的特点结合Redis的list中的push和pop操作进行封装
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisQueueTest {
    public static String redisKey = "redisqueue";
    public static int msgSize = 10000;

    @Resource
    private RedisService redisService;

    @Test
    public void test(){
        init();
        System.out.println("1万条消息生产完成.....");
        pop();
    }

    // 1万消息,消息队列的生产者
    private void init(){
        for (int i = 1; i <= msgSize; i++) {
            String body = "{key:"+ i +"}";
            Message message = new Message("" +i, "topic", "tag", "053a842ce01e4c5d9295b064634a268b", new Date(), body);
            redisService.lpush(redisKey, JSON.toJSONString(message));
        }
    }

    // 1万消息,消息队列的消费者
    private void pop(){
        for (int i = 0; i < msgSize; i++) {
            String str = redisService.rpop(redisKey);
            Message message = JSON.parseObject(str, Message.class);
            if (message != null){
                System.out.println("msgId:" + message.getMsgId() + JSON.toJSONString(message));
            }
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class Message implements Serializable{
        private String msgId;
        private String topic;
        private String tag;
        private String key;
        private Date storeTime;
        private String msgBody;
    }
}
