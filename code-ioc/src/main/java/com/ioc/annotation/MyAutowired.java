package com.ioc.annotation;

import java.lang.annotation.*;

/**
 * @Author: cxx
 * @Date: 2019/10/3 11:39
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MyAutowired {
}
