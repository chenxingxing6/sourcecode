package com.cglib;

import com.UserService;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/1 22:49
 */
public class CglibTest {
    public static void main(String[] args) {
        //代理类class文件存入本地磁盘
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "F:\\code");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserService.class);
        enhancer.setCallback(new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
                methodProxy.invokeSuper(o, objects);
                System.out.println("after");
                return null;
            }
        });
        UserService userServiceProxy = (UserService) enhancer.create();
        userServiceProxy.delete("100");
    }
}
