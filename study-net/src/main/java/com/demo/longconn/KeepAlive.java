package com.demo.longconn;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: lanxinghua
 * Date: 2019/11/4 18:45
 * Desc: 维持连接的消息对象，心跳对象
 */
public class KeepAlive implements Serializable {
    private String clientName;

    public KeepAlive(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\t维持连接包";
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
