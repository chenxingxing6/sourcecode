## 使用Java实现简易版Redis服务器
#### redis
> Redis是一个开源的使用ANSI C语言编写、遵守BSD协议、支持网络、可基于内存亦可持久化的日志型、
key-value数据库。它通常被称为数据结构服务器，因为值（value）可以是字符串（String），哈希
（Hash），列表（List），集合（Set）和有序集合（sorted Set）等类型。

---
#### 实现内容
> 1.基本指令操作  
> 2.多用户并发  
> 3.数据持久化  

---
#### 实现思路
1.将字节流依照Redis的协议反列化为Java能够识别的对象,识别指令。
2.然后通过反射机制，找到对于处理类进行处理。
3.最后将结果依照协议序列化为字节流写到客户端。

Redis的BSD协议   
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/1.jpg)

---
#### redis客户端测试   
软件自己去下载：   
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/9.png)    


1.检测 redis 服务是否启动
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/2.png)    


2.字符串(String)    
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/3.png)    

3.哈希(Hash)    
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/4.png)    

4.列表(List)   
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/5.png)    

5.集合(Set)   
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/6.png)    


持久化的文件：   
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/7.png)    


---

#### 执行日志
```html
{key={field=update}}从文件中读数据
数据加载完成....
执行命令：PINGCommand
执行命令：SETCommand
执行命令：GETCommand
执行命令：HSETCommand
向文件中写入数据{mykey={field=value}, key={field=update}}
执行命令：HGETCommand
执行命令：HGETCommand
执行命令：HGETCommand
执行命令：LPUSHCommand
向文件中写入数据{lxh=[value], key=[]}
执行命令：LPUSHCommand
向文件中写入数据{lxh=[value1, value], key=[]}
执行命令：LPUSHCommand
向文件中写入数据{lxh=[value2, value1, value], key=[]}
执行命令：LPOPCommand
向文件中写入数据{lxh=[value1, value], key=[]}
执行命令：LPOPCommand
向文件中写入数据{lxh=[value], key=[]}
执行命令：LPOPCommand
向文件中写入数据{lxh=[], key=[]}
执行命令：SADDCommand
执行命令：SADDCommand
执行命令：SCARDCommand
执行命令：SADDCommand
执行命令：SCARDCommand

```

---
#### Jedis测试
```java
import redis.clients.jedis.Jedis;

/**
 * @Author: cxx
 * @Date: 2019/11/3 23:28
 */
public class com.demo.JedisTest {
    public static void main(String[] args) {
        Jedis jedis=new Jedis("localhost", 6379);
        String result = jedis.ping();
        System.out.println(result);
        //关闭jedis
        jedis.close();
    }
}
```

```html
Exception in thread "main" redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketTimeoutException: connect timed out
	at redis.clients.jedis.Connection.connect(Connection.java:207)
	at redis.clients.jedis.BinaryClient.connect(BinaryClient.java:93)
	at redis.clients.jedis.Connection.sendCommand(Connection.java:126)
	at redis.clients.jedis.Connection.sendCommand(Connection.java:121)
	at redis.clients.jedis.BinaryClient.ping(BinaryClient.java:106)
	at redis.clients.jedis.BinaryJedis.ping(BinaryJedis.java:195)
	at com.demo.JedisTest.main(com.demo.JedisTest.java:10)
Caused by: java.net.SocketTimeoutException: connect timed out
	at java.net.DualStackPlainSocketImpl.waitForConnect(Native Method)
	at java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:85)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188)
	at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
	at java.net.Socket.connect(Socket.java:589)
	at redis.clients.jedis.Connection.connect(Connection.java:184)
	... 6 more
```

开启redis服务
```html
{lxh=[], key=[]}从文件中读数据
{mykey={field=value}, key={field=update}}从文件中读数据
数据加载完成....
```    
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/8.png)    




