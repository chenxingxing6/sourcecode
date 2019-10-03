package com.demo1;

import com.aop.annotation.*;
import com.aop.aspect.AbstractAspect;
import com.aop.aspect.JoinPoint;
import com.ioc.annotation.MyComponent;

/**
 * @Author: cxx
 * @Date: 2019/10/3 11:25
 * 日志切面
 */
@MyAspect
@MyComponent
public class LogAspect extends AbstractAspect{

    @MyPointcut("com.demo1.*")
    private void myPointcut(){

    }

    @MyAround(value = "myPointcut()")
    public Object around(JoinPoint joinPoint){
        Long start = System.currentTimeMillis();
        System.out.println("环绕通知start....");
        Object obj= joinPoint.proceed();
        System.out.println("环绕通知end....");
        Long end = System.currentTimeMillis();
        System.out.println("执行方法耗时：" + String.valueOf(end - start));
        return obj;
    }
}
