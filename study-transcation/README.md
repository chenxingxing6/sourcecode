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

