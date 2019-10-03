package com.aop.core;

import com.aop.aspect.CheckResult;
import javafx.util.Pair;

import java.lang.reflect.Proxy;

/**
 * @Author: cxx
 * @Date: 2019/10/3 15:38
 */
public class ProxyBeanFactory {
    public static Object newProxyBean(Object target, Pair<Object, CheckResult> aspectResult){
        Class<?>[] interfaces = target.getClass().getInterfaces();
        if (interfaces.length == 0){
            throw new RuntimeException("proxy instance must be implements interface");
        }
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), interfaces, new BeanInvocationHandler(target, aspectResult));
    }
}
