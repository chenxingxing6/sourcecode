package com.aop.aspect;


/**
 * @Author: cxx
 * @Date: 2019/10/3 16:14
 */
public class AbstractAspect {

    public void before(){

    }

    public void after(){

    }

    public void afterReturning(Object returnVal){

    }

    public Object around(JoinPoint joinPoint){
        return null;
    }

    public void afterThrowable(Throwable e){

    }
}
