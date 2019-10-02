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

        // 其实就是生成一个代理类
        IUserService proxyMy = new $ProxyMy(new MyInvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("before");
                method.invoke(new UserService(), args);
                System.out.println("after");
                return null;
            }
        });
        //proxyMy.delete("100");
    }
}
