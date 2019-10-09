package com.catalina.server;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:31
 * Desc: 服务启动
 */
public class BootStrap {
    public static void main(String[] args) {
        new Tomcat().start(8080);
    }
}
