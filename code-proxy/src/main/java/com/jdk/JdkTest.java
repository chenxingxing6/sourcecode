package com.jdk;

import com.IUserService;
import com.UserService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: cxx
 * @Date: 2019/10/1 22:41
 */
public class JdkTest {
    public static void main(String[] args) {
        IUserService userServiceProxy = (IUserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                UserService.class.getInterfaces(),
                new InvocationHandler() {
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
