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
