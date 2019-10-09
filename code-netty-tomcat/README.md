## Netty实现Tomcat
> 1.请求url支持正则表达式  
> 2.servlet在配置文件中配置  

---
```html
servlet.test=testServlet
servlet.test.className=com.catalina.servlet.TestServlet
servlet.test.urlPattern=/test*
```

http://localhost:8080/test?name=2044   

---

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/1.jpg)    

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/2.jpg)    

---
## 热部署实现
##### 类加载器
> Java类的加载是由虚拟机来完成的,虚拟机把描述类的Class文件加载到内存,并对数据进行校验,解析和初始化,最终形成能被Java虚拟机直接使用的Java类型,
这就是虚拟机的类加载机制.JVM中用来完成上述功能的具体实现就是类加载器.类加载器读取.class字节码文件将其转换成java.lang.Class类的一个实例.每个
实例用来表示一个java类.通过该实例的newInstance()方法可以创建出一个该类的对象.

---
  
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/3.jpg)    

---
![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-netty-tomcat/img/4.jpg)    

---
##### 热部署
> 对于Java应用程序来说，热部署就是在运行时更新Java类文件。也就是不重启服务器的情况下实现java类文件的替换修改等.举个例子，就像电脑可以在不重启
的情况下，更换U盘。简单一句话让JVM重新加载新的class文件!

想要实现热部署可以分以下步骤:
> 1.监听修改文件，生成class文件，并进行替换  
> 2.创建新的类加载器，加载更新的class文件

---



