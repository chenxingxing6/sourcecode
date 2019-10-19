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
