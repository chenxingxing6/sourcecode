## 自己实现简单Java缓存
#### 需求
> 1.数据共享  
> 2.过期自动失效

#### 实现思路
采用HashMap+定时器线程池实现，map用于存储键值对数据，map的value是Cache的内部类对
象Entity，Entity包含value和该键值对的生命周期定时器Future。
---
