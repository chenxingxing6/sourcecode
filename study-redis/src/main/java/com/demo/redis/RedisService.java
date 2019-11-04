package com.demo.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class RedisService {

	@Autowired
    private JedisPool jedisPool;

	public boolean setnx(String key, String value){
		Jedis jedis = null;
		try {
			jedis =  jedisPool.getResource();
			Long result = jedis.setnx(key, value);
			if (result > 0){
				return true;
			}
		}finally {
			returnToPool(jedis);
		}
		return false;
	}


	public boolean setnx(String key, String value, int expireSecond){
		Jedis jedis = null;
		try {
			jedis =  jedisPool.getResource();
			if (expireSecond == -1){
				return jedis.setnx(key, value) > 0 ? true : false;
			}
			// nx：不存在才设置  ex：秒
			String statusCode = jedis.set(key, value, "NX", "EX", expireSecond);
			return (statusCode != null && statusCode.equalsIgnoreCase("OK")) ? true : false;
		}finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 获取当个对象
	 * */
	public <T> T get(String key,  Class<T> clazz) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String  str = jedis.get(key);
			 T t =  stringToBean(str, clazz);
			 return t;
		 }finally {
			  returnToPool(jedis);
		 }
	}

	public  Long expice(String key,int exTime){
		Jedis jedis = null;
		Long result = null;
		try {
			jedis =  jedisPool.getResource();
			result = jedis.expire(key,exTime);
			return result;
		} finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 设置对象
	 * */
	public <T> boolean set(String key,  T value ,int exTime) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			 String str = beanToString(value);
			 if(str == null || str.length() <= 0) {
				 return false;
			 }
			//生成真正的key
			 if(exTime == 0) {
			 	 //直接保存
				 jedis.set(key, str);
			 }else {
			 	 //设置过期时间
				 jedis.setex(key, exTime, str);
			 }
			 return true;
		 }finally {
			  returnToPool(jedis);
		 }
	}

	/**
	 * 删除key
	 * @param key
	 * @return
	 */
	public Long del(String key){
		Jedis jedis = null;
		Long result = null;
		try {
			jedis =  jedisPool.getResource();
			result = jedis.del(key);
			return result;
		} finally {
			returnToPool(jedis);
		}
	}


	/**
	 * 判断key是否存在
	 * */
	public <T> boolean exists(String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			return  jedis.exists(key);
		 }finally {
			  returnToPool(jedis);
		 }
	}

	/**
	 * 增加值
	 * */
	public <T> Long incr(String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			//生成真正的key
			return  jedis.incr(key);
		 }finally {
			  returnToPool(jedis);
		 }
	}

	/**
	 * 减少值
	 * */
	public <T> Long decr(String key) {
		 Jedis jedis = null;
		 try {
			 jedis =  jedisPool.getResource();
			return  jedis.decr(key);
		 }finally {
			  returnToPool(jedis);
		 }
	}

	/**
     * 存储redis队列，顺序存储
	 * @param key
     * @param value
	 */
	public void lpush(String key, String value){
		Jedis jedis = null;
		try {
			jedis =  jedisPool.getResource();
			jedis.lpush(key, value);
		}finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 存储redis队列,反向存储
	 * @param key
	 * @param value
	 */
	public void rpush(String key, String value){
		Jedis jedis = null;
		try {
			jedis =  jedisPool.getResource();
			jedis.rpush(key, value);
		}finally {
			returnToPool(jedis);
		}
	}

	/**
	 * 获取队列数据
	 * @param key
	 * @param value
	 */
	public String rpop(String key){
		Jedis jedis = null;
		try {
			jedis =  jedisPool.getResource();
			return jedis.rpop(key);
		}finally {
			returnToPool(jedis);
		}
	}



	/**
	 * bean 转 String
	 * @param value
	 * @param <T>
	 * @return
	 */
	public static <T> String beanToString(T value) {
		if(value == null) {
			return null;
		}
		Class<?> clazz = value.getClass();
		if(clazz == int.class || clazz == Integer.class) {
			 return ""+value;
		}else if(clazz == String.class) {
			 return (String)value;
		}else if(clazz == long.class || clazz == Long.class) {
			return ""+value;
		}else {
			return JSON.toJSONString(value);
		}
	}


	/**
	 * string转bean
	 * @param str
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T stringToBean(String str, Class<T> clazz) {
		if(str == null || str.length() <= 0 || clazz == null) {
			 return null;
		}
		if(clazz == int.class || clazz == Integer.class) {
			 return (T)Integer.valueOf(str);
		}else if(clazz == String.class) {
			 return (T)str;
		}else if(clazz == long.class || clazz == Long.class) {
			return  (T)Long.valueOf(str);
		}else {
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}

	private void returnToPool(Jedis jedis) {
		 if(jedis != null) {
			 jedis.close();
		 }
	}

}
