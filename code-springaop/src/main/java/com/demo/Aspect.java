package com.demo;

import com.aop.annotation.*;
import com.aop.aspect.AbstractAspect;
import com.aop.aspect.JoinPoint;
import com.ioc.annotation.MyComponent;

/**
 * @Author: cxx
 * @Date: 2019/10/3 11:25
 * 定义切面
 */
@MyAspect
@MyComponent
public class Aspect extends AbstractAspect{

    @MyPointcut("com.demo.*")
    private void myPointcut(){

    }

    @MyBefore(value = "myPointcut()")
    public void before(){
        System.out.println("前置通知....");

    }

    @MyAfter(value = "myPointcut()")
    public void after(){
        System.out.println("后置通知....");

    }

    @MyAfterReturning(value = "myPointcut()")
    public void afterReturning(Object returnVal){
        System.out.println("后置返回通知....result:" + returnVal.toString());

    }

    @MyAround(value = "")
    public Object around(JoinPoint joinPoint){
        System.out.println("环绕通知start....");
        Object obj= joinPoint.proceed();
        System.out.println("环绕通知end....");
        return null;

    }

    //@MyAfterThrowing(value = "myPointcut()")
    public void afterThrowable(Throwable e){
        System.out.println("异常通知....");
        e.printStackTrace();
    }
}
