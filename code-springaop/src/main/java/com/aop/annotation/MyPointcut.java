package com.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author: cxx
 * @Date: 2019/10/3 10:54
 * 切点
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyPointcut {
    Class<?> clazz();
}
