package com.demo;

import com.ioc.core.MyIoc;


/**
 * @Author: cxx
 * @Date: 2019/10/3 11:34
 */
public class MainTest {
    public static void main(String[] args) {
        IUserService userService = (IUserService) MyIoc.getObject(UserService.class.getName());
        userService.delete("100");
    }
}
