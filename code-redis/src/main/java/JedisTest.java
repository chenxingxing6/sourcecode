import redis.clients.jedis.Jedis;

/**
 * @Author: cxx
 * @Date: 2019/11/3 23:28
 */
public class JedisTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost", 6379);
        String result = jedis.ping();
        System.out.println(result);
        //关闭jedis
        jedis.close();
    }
}
