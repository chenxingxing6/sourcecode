## Netty实现Tomcat & 热部署自己实现
> 1.请求url支持正则表达式  
> 2.servlet在配置文件中配置  

---
```html
servlet.test=testServlet
servlet.test.className=com.catalina.servlet.TestServlet
servlet.test.urlPattern=/test*
```

http://localhost:8080/test?name=2044   

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/1.jpg)    

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/2.jpg)    

---

```java
package com.catalina.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:30
 * Desc:
 */
public class Tomcat {

    /**
     * 开启服务
     * @param port
     * @throws Exception
     */
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            // 配置
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    // 主线程处理类
                    .channel(NioServerSocketChannel.class)
                    // 子线程处理类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            // response编码
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            // request解码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            // 业务逻辑处理
                            socketChannel.pipeline().addLast(new TomcatHandler());
                        }
                    })
                    // 对主线程，最大分配128个线程
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 子线程保存长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            // 启动服务器
            ChannelFuture f = bootstrap.bind(port).sync();
            System.out.println("Netty Tomcat Server Is Start...http://localhost:" + port);
            // 主线程wait
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
```
---
## 热部署实现
##### 类加载器
> Java类的加载是由虚拟机来完成的,虚拟机把描述类的Class文件加载到内存,并对数据进行校验,解析和初始化,最终形成能被Java虚拟机直接使用的Java类型,
这就是虚拟机的类加载机制.JVM中用来完成上述功能的具体实现就是类加载器.类加载器读取.class字节码文件将其转换成java.lang.Class类的一个实例.每个
实例用来表示一个java类.通过该实例的newInstance()方法可以创建出一个该类的对象.

---
  
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/3.png)    

---
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/4.png)    

---
##### 热部署
> 对于Java应用程序来说，热部署就是在运行时更新Java类文件。也就是不重启服务器的情况下实现java类文件的替换修改等.举个例子，就像电脑可以在不重启
的情况下，更换U盘。简单一句话让JVM重新加载新的class文件!

想要实现热部署可以分以下步骤:
> 1.监听修改文件，生成class文件，并进行替换  
> 2.创建新的类加载器，加载更新的class文件

---
```java
package com.hot;

import sun.nio.ch.IOUtil;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/10/9 15:00
 * Desc: 开启热部署,com.catalina.servlet下的文件
 * 修改servlet文件，保存，会自动实现热更新部署
 */
public class FileMonitor {
    private static final String projectName = "code-netty-tomcat";
    private static final String packagePath = "com/catalina/servlet/";

    public static void main(String[] args) {
       new FileMonitor().start();
    }

    public void start(){
        System.out.println("开启热部署.....");
        try {
            Path path = Paths.get(projectName + "/src/main/java/" + packagePath);
            WatchService watcher = FileSystems.getDefault().newWatchService();
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
            new Thread(() -> {
                while (true) {
                    try {
                        WatchKey key = watcher.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            if (event.kind() == StandardWatchEventKinds.OVERFLOW){
                                // 事件可能是lost or discarded
                                continue;
                            }
                            Path p = (Path) event.context();
                            System.out.println("------------ start 热部署 --------------");
                            hotDeploy(p);
                            System.out.println("------------- end 热部署 --------------");
                        }
                        if (!key.reset()){
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            TimeUnit.SECONDS.sleep(60*10);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 热部署
     * @param path
     */
    public static void hotDeploy(Path path){
        String fileName = path.toFile().getName();
        // java源码路径
        String prefixPath = projectName + "/src/main/java/" + packagePath;
        String sourceCodePath = prefixPath+fileName;
        fileName = fileName.replace(".java", ".class");
        try {
            System.gc();
            String p = projectName + "/target/classes/"+ packagePath +fileName;
            File oldFile = new File(p);
            oldFile.delete();
            // 对源码进行编译
            doCompile(sourceCodePath);
            // 编译后端class文件移动到target对应的目录中去
            moveFile(prefixPath + fileName, p);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void doCompile(String sourceCodePath){
        try {
            System.out.println("源码文件进行编译："+sourceCodePath);
            File file = new File(sourceCodePath);
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable iterable = manager.getJavaFileObjects(file);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void moveFile(String srcPathStr, String desPathStr) {
        try{
            // 创建输入输出流对象
            File file = new File(desPathStr);
            if (!file.exists()){
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(srcPathStr);
            FileOutputStream fos = new FileOutputStream(desPathStr);
            //创建搬运工具
            byte datas[] = new byte[1024*8];
            //创建长度
            int len = 0;
            //循环读取数据
            while((len = fis.read(datas))!=-1){
                fos.write(datas,0,len);
            }
            //释放资源
            fis.close();
            fis.close();
            File srcFile = new File(srcPathStr);
            srcFile.delete();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
```
---
修改com.catalina.servlet.TestServlet文件：
```java
public class TestServlet extends Servlet {
    public void doGet(Request req, Response resp) throws Exception {
        this.doPost(req, resp);
    }

    public void doPost(Request req, Response resp) throws Exception {
        String name = req.getParamter("name");
        resp.setCharSet("UTF-8");
        resp.setContentType("text/html; charset=utf-8");
        String html = "<h2>自己手写Netty实现Tomcat&热部署</h2><hr>";
        html += "<div>name："+ name +"<div>";
        
        // 修改的东西
        html += "update.....";
        
        resp.write(html);
    }
}
```
---
文件监听日志
```html
------------ start 热部署 --------------
源码文件进行编译：code-netty-tomcat/src/main/java/com/catalina/servlet/TestServlet.java
------------- end 热部署 --------------

```
---
浏览器请求结果

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/5.jpg)    

---




