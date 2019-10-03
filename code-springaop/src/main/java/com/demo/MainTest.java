package com.demo;

import com.aop.core.AopBeanContext;
import com.demo1.ILogService;
import com.demo1.LogService;

/**
 * @Author: cxx
 * @Date: 2019/10/3 11:34
 */
public class MainTest {
    public static void main(String[] args) {
        //  01.测试 修改ioc.properties scan.package=com.demo
        //IUserService userService = (IUserService) AopBeanContext.getObject(UserService.class);
        //userService.delete("100");

        // 02.环绕通知测试 修改ioc.properties scan.package=com.demo1
        ILogService logService = (ILogService) AopBeanContext.getObject(LogService.class);
        logService.printLog("test....");
    }
}
