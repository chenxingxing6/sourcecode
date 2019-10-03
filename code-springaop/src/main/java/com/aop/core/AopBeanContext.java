package com.aop.core;

import com.aop.aspect.AspectHandler;
import com.aop.aspect.CheckResult;
import com.aop.aspect.IAspectHandler;
import com.ioc.core.MyIoc;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: cxx
 * @Date: 2019/10/3 13:45
 */
public class AopBeanContext {
    private static IAspectHandler aspectHandler = null;

    static {
        init();
    }

    public static Object getObject(Class clz){
        Object target = MyIoc.getObject(clz);
        if (target == null){
            throw new RuntimeException("容器中获取不到实例");
        }
        Pair<Object, CheckResult> aspectResult = aspectHandler.getAspectInstance(clz.getTypeName());
        // 没有切面
        if (aspectResult == null){
            return target;
        }
        // 创建代理类
        return ProxyBeanFactory.newProxyBean(target, aspectResult);
    }

    private static void init(){
        createProxyBeanContext(MyIoc.getBeanFactory());
    }

    // 根据切点，创建有代理类的bean容器
    public static void createProxyBeanContext(Map<String, Object> iocMap){
        aspectHandler = new AspectHandler(iocMap);
    }
}
