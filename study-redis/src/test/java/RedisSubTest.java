import com.demo.RedisApplication;
import com.demo.pubsub.Consumer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/11/4 16:36
 * Desc: redis发布订阅测试-订阅
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = RedisApplication.class)
public class RedisSubTest {
    @Resource
    private Consumer pubsubConsumer;

    @Test
    public void subscribe() throws Exception{
        pubsubConsumer.subscribe();
    }
}
