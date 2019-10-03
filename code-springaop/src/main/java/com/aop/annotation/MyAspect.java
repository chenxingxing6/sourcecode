package com.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author: cxx
 * @Date: 2019/10/3 10:52
 * 定义切面
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MyAspect {
}
