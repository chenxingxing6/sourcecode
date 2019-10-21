package com.demo.redis;

/**
 * User: lanxinghua
 * Date: 2019/10/21 14:30
 * Desc:
 */
public class BasePrefix implements KeyPrefix {
    private String prefix;

    public BasePrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
