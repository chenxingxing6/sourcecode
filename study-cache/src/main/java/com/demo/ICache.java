package com.demo;

/**
 * @Author: cxx
 * @Date: 2019/11/3 18:00
 */
public interface ICache {
    public void put(String key, Object value);

    public void put(String key, Object value, Long secondExpire);

    public <T> T get(String key);

    public <T> T remove(String key);
}
