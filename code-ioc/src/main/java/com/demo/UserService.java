package com.demo;

import com.ioc.annotation.MyAutowired;
import com.ioc.annotation.MyComponent;

/**
 * @Author: cxx
 * @Date: 2019/10/1 22:40
 */
@MyComponent
public class UserService implements IUserService {
    @MyAutowired
    private LogService logService;

    public void delete(String id) {
        System.out.println("删除用户 id:" + id);
        logService.print("删除用户");
    }
}
