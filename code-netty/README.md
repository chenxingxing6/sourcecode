## 手写简化版netty
netty 源码比较复杂，考虑了许多可扩展、安全、性能等，netty 的核心就隐藏在这百花深处。我们就考虑核心的东西，实现简单版的netty，
更好理解netty核心原理。

## netty原理

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty/img/1.jpg)    


##### Reactor线程模型
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty/img/2.jpg)    

请求过程：
> 1.客户端请求，reactor线程进行处理accept，专门负责轮询   
> 2.分发请求给对应的worker线程   
> 3.worker线程更加配置，继续分发或者处理请求

MainReactorThread 与 SubReactorThread 线程，分别用于处理 ACCEPT 事件与其它 I/O 事件。
MainReactorThread：

> 在 main 方法中被调用 start 方法；   
只注册了 ServerSocketChanel 的 ACCETP 事件；   
知道全部 SubReactorThread 对象；   
具有一个从 SubReactorThread 组中挑选出某一个 Thread 的方法；   
具有启动 SubReactorThread 的方法；   
将 channnel 视为 ServerSocketChannel；   
调用 ServerSocketChannel.accept() 方法取得一个客户端的 SocketChannel；   
把 SocketCahnnel 注册到 I/O 线程，关注 READ 事件。   

SubReactorThread：

> 在 MainReactorThread 方法中被调用 start 方法；   
只注册了 SocketChannel 的 READ 事件；   
知道更多的 workThread，用于业务操作；   
具有挑选 workThread 的方法；   
从 channel 中读取数据；   
把业务操作提交到 workThread；    
写入响应数据；    


