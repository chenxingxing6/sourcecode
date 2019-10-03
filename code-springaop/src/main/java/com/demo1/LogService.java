package com.demo1;

import com.ioc.annotation.MyComponent;

import java.util.concurrent.TimeUnit;

/**
 * @Author: cxx
 * @Date: 2019/10/3 16:56
 */
@MyComponent
public class LogService implements ILogService {
    @Override
    public void printLog(String log) {
        System.out.println("日志打印成功 ：" + log);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
