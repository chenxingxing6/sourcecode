package com.catalina.servlet;

import com.catalina.http.Request;
import com.catalina.http.Response;
import com.catalina.http.Servlet;

/**
 * User: lanxinghua
 * Date: 2019/10/9 09:29
 * Desc:
 */
public class TestServlet extends Servlet {
    public void doGet(Request req, Response resp) throws Exception {
        this.doPost(req, resp);
    }

    public void doPost(Request req, Response resp) throws Exception {
        String name = req.getParamter("name");
        resp.write("ok....." + name);
    }
}
