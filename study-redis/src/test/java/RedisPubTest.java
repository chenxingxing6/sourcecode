import com.demo.RedisApplication;
import com.demo.pubsub.Producer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * User: lanxinghua
 * Date: 2019/11/4 16:36
 * Desc: redis发布订阅测试-发布
 * {"key":"053a842","msgBody":"{key:57}","msgId":"57","storeTime":1572857690022,"tag":"tag","topic":"CHANNEL"}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisPubTest {
    @Resource
    private Producer pubsubProducer;

    @Test
    public void publish(){
        pubsubProducer.producer();
    }
}
