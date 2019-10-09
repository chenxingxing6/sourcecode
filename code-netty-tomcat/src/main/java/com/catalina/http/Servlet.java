package com.catalina.http;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:27
 * Desc: 自定义servlet
 */
public abstract class Servlet {

    public abstract void doGet(Request req, Response resp) throws Exception;

    public abstract void doPost(Request req, Response resp) throws Exception;
}
