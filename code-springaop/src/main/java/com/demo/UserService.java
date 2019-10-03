package com.demo;


import com.ioc.annotation.MyAutowired;
import com.ioc.annotation.MyComponent;

/**
 * @Author: cxx
 * @Date: 2019/10/1 22:40
 */
@MyComponent
public class UserService implements IUserService {
    public String delete(String id) {
        System.out.println("删除用户 id:" + id);
        int i = 1/0;
        return "删除成功";
    }
}
