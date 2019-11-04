package com.demo.longconn.server;

/**
 * @Author: cxx
 * @Date: 2019/11/4 22:34
 * 客户端服务器状态
 */
public class ClientState {
    private int isValid = 0;
    private String clientName;
    private long lastReTime;

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public long getLastReTime() {
        return lastReTime;
    }

    public void setLastReTime(long lastReTime) {
        this.lastReTime = lastReTime;
    }

    @Override
    public String toString() {
        return "ClientState{" +
                "isValid=" + isValid +
                ", clientName='" + clientName + '\'' +
                ", lastReTime=" + lastReTime +
                '}';
    }
}
