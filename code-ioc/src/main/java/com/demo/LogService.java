package com.demo;

import com.ioc.annotation.MyComponent;

/**
 * @Author: cxx
 * @Date: 2019/10/3 12:50
 */
@MyComponent
public class LogService {
    public void print(String msg){
        System.out.println("print log:" + msg);
    }
}
