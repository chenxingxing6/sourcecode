package com.aop.annotation;

/**
 * @Author: cxx
 * @Date: 2019/10/3 15:58
 * 通知方式
 */
public enum AdviceEnum {
    BEFORE("before"),
    AFTER("after"),
    AROUND("around"),
    AFTER_RETURNING("afterReturning"),
    AFTER_THROWING("afterThrowing");

    AdviceEnum(String key) {
        this.key = key;
    }

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
