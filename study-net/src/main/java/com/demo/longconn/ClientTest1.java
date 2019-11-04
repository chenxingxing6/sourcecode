package com.demo.longconn;

import com.demo.longconn.client.Client;

/**
 * User: lanxinghua
 * Date: 2019/11/4 19:32
 * Desc:
 */
public class ClientTest1 {
    public static void main(String[] args) {
        for (int i = 1; i <=3; i++) {
            new Client("localhost", 9999, "客户端" + i).start();
        }
    }
}
