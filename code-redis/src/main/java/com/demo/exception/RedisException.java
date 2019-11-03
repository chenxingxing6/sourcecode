package com.demo.exception;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:56
 */
public class RedisException extends RuntimeException{
    public RedisException(String msg){
        super(msg);
        System.out.println("Exception：" + msg);
    }
}
