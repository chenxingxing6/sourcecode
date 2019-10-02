package com.my;

import com.IUserService;

import java.lang.reflect.Method;

/**
 * @Author: cxx
 * @Date: 2019/10/2 11:29
 * 动态生成代理类：如下
 */
public class $ProxyMy implements IUserService {
    private MyInvocationHandler invocationHandler;

    public $ProxyMy(MyInvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    public void delete(String id) {
        try {
            Method m = IUserService.class.getMethod("delete", String.class);
            invocationHandler.invoke(this, m, new Object[]{id});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
