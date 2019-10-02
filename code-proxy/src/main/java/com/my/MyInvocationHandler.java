package com.my;

import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/2 11:11
 */
public interface MyInvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
