package com.aop.annotation;

import java.lang.annotation.*;

/**
 * @Author: cxx
 * @Date: 2019/10/3 10:52
 * 后置通知
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyAfter {
    String value();
}
