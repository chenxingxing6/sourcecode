## 自己实现代理类MyProxy,步骤分为以下5步

> 1.生成java源码
2.將源码输出到java文件中   
3.将java文件编译成class文件
4.将class加载进jvm  
5.返回代理类对象  

---
测试
```java
package com.my;

import com.IUserService;
import com.UserService;

import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/2 11:13
 */
public class MyTest {
    public static void main(String[] args) {
        IUserService userServiceProxy = (IUserService) MyProxy.newProxyInstance(
                new MyClassLoader(),
                UserService.class.getInterfaces(),
                new MyInvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("before");
                        method.invoke(new UserService(), args);
                          System.out.println("after");
                        return null;
                    }
                }
        );
        userServiceProxy.delete("100");
    }
}
```

---
MyClassLoader
```java
package com.my;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @Author: cxx
 * @Date: 2019/10/2 14:24
 */
public class MyClassLoader extends ClassLoader{
    private String baseDir;

    public MyClassLoader() {
        this.baseDir = this.getClass().getResource("").getFile();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String className = this.getClass().getPackage().getName() + "." + name;
        if (baseDir != null && baseDir != ""){
            File f = new File(baseDir + "/" + name +".class");
            ByteArrayOutputStream out = null;
            FileInputStream fis = null;
            if (f.exists()){
                try {
                    fis = new FileInputStream(f);
                    out = new ByteArrayOutputStream();
                    byte[] b = new byte[1024];
                    int len;
                    while ((len = fis.read(b)) != -1){
                        out.write(b, 0, len);
                    }
                    return defineClass(className, out.toByteArray(), 0, out.size());
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (fis != null){
                        try {
                            fis.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    if (out != null){
                        try {
                            out.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    f.delete();
                }
            }
        }
        return null;
    }
}
```
---
MyInvocationHandler
```java
package com.my;
import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/2 11:11
 */
public interface MyInvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}

```
---
MyProxy
```java
package com.my;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/2 11:14
 */
public class MyProxy implements Serializable {
    private static final String ln = "\r\n";
    public static Object newProxyInstance(MyClassLoader classLoader, Class<?>[] interfaces, MyInvocationHandler myInvocationHandler){
        File file = null;
       try {
           // 1.获取java源码,并输出到文件
           String source = generateSrc(interfaces);
           String filePath = MyProxy.class.getResource("").getPath();
           file = new File(filePath + "$Proxy0.java");
           FileWriter fw = new FileWriter(file);
           fw.write(source);
           fw.flush();
           fw.close();

           // 2.对源码进行编译
           JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
           StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
           Iterable iterable = fileManager.getJavaFileObjects(file);
           JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, iterable);
           task.call();
           fileManager.close();

           // 3.加载class文件到jvm
           Class proxyClass = classLoader.findClass("$Proxy0");

           // 4.返回代理对象
           Constructor c = proxyClass.getConstructor(MyInvocationHandler.class);
           return c.newInstance(myInvocationHandler);
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           file.delete();
       }
       return null;
    }

    /**
     * 获取源码，动态生成java类，看$ProxyMy
     * @param interfaces
     * @return
     */
    private static String generateSrc(Class<?>[] interfaces) {
        StringBuffer sb = new StringBuffer();
        sb.append("package com.my;" + ln);
        sb.append("import java.lang.reflect.Method;" + ln);
        sb.append("public class $Proxy0 implements " + interfaces[0].getName() + "{" + ln);
        sb.append("private MyInvocationHandler h;"+ln);
        sb.append("public $Proxy0(MyInvocationHandler h) { " + ln);
        sb.append("this.h = h;"+ln);
        sb.append("}" + ln);
        for (Method m : interfaces[0].getMethods()) {
            sb.append("public " + m.getReturnType().getName() + " "
                    + m.getName() + "(String id) {" + ln);
            sb.append("try{" + ln);
            sb.append("Method m = " + interfaces[0].getName()
                    + ".class.getMethod(\"" + m.getName()
                    + "\",String.class);" + ln);
            sb.append("this.h.invoke(this,m,new Object[]{id});" + ln);
            sb.append("}catch(Throwable e){" + ln);
            sb.append("e.printStackTrace();" + ln);
            sb.append("}"+ln);
            sb.append("}"+ln);
        }
        sb.append("}" + ln);
        return sb.toString();
    }
}
```
---
结果
> Connected to the target VM, address: '127.0.0.1:49209', transport: 'socket'   
  Disconnected from the target VM, address: '127.0.0.1:49209', transport: 'socket'   
  before   
  删除用户 id:100   
  after
  
  ---
生成的代理类
```java
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.my;

import com.IUserService;
import java.lang.reflect.Method;

public class $Proxy0 implements IUserService {
    private MyInvocationHandler h;

    public $Proxy0(MyInvocationHandler var1) {
        this.h = var1;
    }

    public void delete(String var1) {
        try {
            Method var2 = IUserService.class.getMethod("delete", String.class);
            this.h.invoke(this, var2, new Object[]{var1});
        } catch (Throwable var3) {
            var3.printStackTrace();
        }
    }
}
```

![avatar](https://raw.githubusercontent.com/chenxingxing6/sourcecode/master/code-proxy/img/1.png)    
---