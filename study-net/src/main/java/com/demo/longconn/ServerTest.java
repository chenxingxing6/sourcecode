package com.demo.longconn;

import com.demo.longconn.server.Server;

/**
 * User: lanxinghua
 * Date: 2019/11/4 19:32
 * Desc:
 */
public class ServerTest {
    public static void main(String[] args) {
        new Server(9999).start();
    }
}
