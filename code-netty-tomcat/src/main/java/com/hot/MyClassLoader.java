package com.hot;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: lanxinghua
 * Date: 2019/10/9 11:52
 * Desc: 实现热部署核心，通过自定义类加载器，创建新的对象，jvm进行加载
 */
public class MyClassLoader extends ClassLoader {
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // 文件名称
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            // 读取文件流
            InputStream is = this.getClass().getResourceAsStream("/com/catalina/servlet/" + fileName);
            // 读取字节
            byte[] b = new byte[is.available()];
            is.read(b);
            //数据给JVM识别Class对象
            return defineClass(name,b, 0, b.length);
        } catch (IOException e) {
            throw new ClassNotFoundException();
        }
    }
}
