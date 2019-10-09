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

