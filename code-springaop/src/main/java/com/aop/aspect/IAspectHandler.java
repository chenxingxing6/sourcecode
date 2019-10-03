package com.aop.aspect;

import javafx.util.Pair;

/**
 * @Author: cxx
 * @Date: 2019/10/3 14:10
 */
public interface IAspectHandler {
    /**
     * 获取切面实例
     * @param pointCanonicalName
     * @return
     */
    public Pair<Object, CheckResult> getAspectInstance(String pointCanonicalName);
}
