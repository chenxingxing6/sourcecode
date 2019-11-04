import com.demo.RedisApplication;
import com.demo.p2p.Consumer;
import com.demo.p2p.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/11/4 11:47
 * Desc: redis实现消息队列
 * 消息队列的先进先出(FIFO)的特点结合Redis的list中的push和pop操作进行封装
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisQueueTest {
    @Resource
    private Producer producer;
    @Resource
    private Consumer consumer;

    @Test
    public void test(){
        push();
        System.out.println("1万条消息生产完成.....");
        pop();
        System.out.println("1万条消息消费完成.....");
    }

    // 1万消息,消息队列的生产者
    private void push(){
        producer.producer();
    }

    // 1万消息,消息队列的消费者
    private void pop(){
        consumer.consumer();
    }
}
