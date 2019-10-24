## 开发中常见的事务处理

---
```sql
CREATE TABLE `user1` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) NOT NULL,
  `is_valid` smallint(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
```

---
```java
package com.demo.txtest;

/**
 * User: lanxinghua
 * Date: 2019/10/19 15:03
 * Desc:
 */
public interface ITxService {
    /**
     * 总结:1和2，外围无事务，哪个方法报错，哪个回滚
     * 1、外围无事务有异常，里面有两个添加方法，可以添加成功
     */
    void notxException_required_required();
    /**
     * 2、外围无事务无异常，里面有两个添加方法，其中一个事务方法内部异常，导致回滚
     */
    void notx_required_requiredException();


    /**
     * 3、外围有事务且有异常，里面有两个添加方法，不论外围有异常，还是其他方法有异常，全都回滚
     */
    void txException_required_required();

    /**
     * 4、外围有事务且有异常,不回滚
     */
    void txException_requiredNew_requiredNew();

    /**
     * 5、外围有事务且有异常，里面有两个添加方法，requiredNewException的进行回滚
     */
    void txException_requiredNew_requiredNewException();

    /**
     * 6、外围有事务且有异常，里面有两个添加方法，外部插入成功，内部回滚
     */
    void txException_required_requiredExceptionTry();
}

```
内部方法调用，事务不起作用？
```html
public void notx_notxMethod_txMethodException() {
    a();
}
public void a(){
    userService.addRequired(user1);
    this.b();
}
@Transactional
public void b(){
    userService.addRequired(user2);
    throw new RuntimeException();
}
```

AOP使用的是动态代理的机制，它会给类生成一个代理类，事务的相关操作都在代理类上完成。内部方式使用
this.b调用方式时，使用的是实例调用，并没有通过代理类调用方法，所以会导致事务失效。

解决办法
> 1.引入自身bean,从而实现使用AOP代理操作  
> 2.通过ApplicationContext引入bean  
> 3.通过AopContext获取当前类的代理类  

---
##### java中锁与@Transactional同时使用导致锁失效的问题
问题分析：
> 由于spring aop会在update方法之前开启事务，之后再加锁，当锁住代码后执行完后再提交事务：finally方法运行完，
删除key后，事务还未提交。导致其他线程进行代码块，读取的数据不是最新的。

解决办法：
> 在update之前就加上锁（还没开启事务前就加上锁）。

这种方式会有问题：
```html
 @Transactional
    public void update(String key){
        boolean lock = lockUtil.lock(LockEnum.TEST, key, false);
        if (!lock){
            throw new RuntimeException("当前人数过多，请稍后再试！");
        }
        try {
            System.out.println("处理业务逻辑: " + key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lockUtil.unlock(LockEnum.TEST, key);
        }
    }

```


多个线程执行了业务逻辑

> 处理业务逻辑: lxh
  Exception in thread "pool-1-thread-9" java.lang.RuntimeException: 当前人数过多，请稍后再试！   
  	at com.demo.txtest.TxRedisService.update(TxRedisService.java:32)   
  	at com.demo.txtest.TxRedisService$$FastClassBySpringCGLIB$$59bcc18.invoke(<generated>)   
  	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)   
  	at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749)   
  	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:163)   
  	at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:294)   
  	at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:98)   
  	at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:186)   
  	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:688)   
  	at com.demo.txtest.TxRedisService$$EnhancerBySpringCGLIB$$35098ed1.update(<generated>)      
  	at com.demo.RedisAndTxTest$1.run(RedisAndTxTest.java:35)   
  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)   
  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)   
  	at java.lang.Thread.run(Thread.java:748)    
  处理业务逻辑: lxh   
  处理业务逻辑: lxh   
  
  
  
---
  
正确编写方式：锁粒度扩大化
```html
 /**
     * 将锁粒度范围扩大
     * @param key
     */
    public void updatev1(String key){
        boolean lock = lockUtil.lock(LockEnum.TEST, key, false);
        if (!lock){
            throw new RuntimeException("当前人数过多，请稍后再试！");
        }
        try {
            txRedisService.innerUpdate(key);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lockUtil.unlock(LockEnum.TEST, key);
        }
    }

    @Transactional
    public void innerUpdate(String key){
        System.out.println("处理业务逻辑: " + key);
    }
```

> java.lang.RuntimeException: 当前人数过多，请稍后再试！   
  	at com.demo.txtest.TxRedisService.updatev1(TxRedisService.java:50)   
  	at com.demo.txtest.TxRedisService$$FastClassBySpringCGLIB$$59bcc18.invoke(<generated>)   
  	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)    
  	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:684)    
  	at com.demo.txtest.TxRedisService$$EnhancerBySpringCGLIB$$682d66db.updatev1(<generated>)     
  	at com.demo.RedisAndTxTest$2.run(RedisAndTxTest.java:51)    
  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)   
  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)    
  	at java.lang.Thread.run(Thread.java:748)   
  java.lang.RuntimeException: 当前人数过多，请稍后再试！    
  	at com.demo.txtest.TxRedisService.updatev1(TxRedisService.java:50)   
  	at com.demo.txtest.TxRedisService$$FastClassBySpringCGLIB$$59bcc18.invoke(<generated>)   
  	at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218)   
  	at org.springframework.aop.framework.CglibAopProxy$DynamicAdvisedInterceptor.intercept(CglibAopProxy.java:684)    
  	at com.demo.txtest.TxRedisService$$EnhancerBySpringCGLIB$$682d66db.updatev1(<generated>)    
  	at com.demo.RedisAndTxTest$2.run(RedisAndTxTest.java:51)   
  	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)   
  	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)   
  	at java.lang.Thread.run(Thread.java:748)   
  处理业务逻辑: lxh    
  
  

