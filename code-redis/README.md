## 使用Java实现简易版Redis服务器
#### 实现内容
> 1.基本指令操作  
> 2.多用户并发  
> 3.数据持久化  

---
#### 实现思路
识别指令，然后通过反射机制，找到对于处理类进行处理。最后将结果依照协议序列化为字节流写到客户端。

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-redis/img/1.jpg)

---
#### 测试
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


