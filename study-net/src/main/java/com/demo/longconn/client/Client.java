package com.demo.longconn.client;

import com.demo.longconn.KeepAlive;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/11/4 18:49
 * Desc: 客户端，定时向服务端程序，发送一个维持连接包的
 */
public class Client {
    private String ip;
    private int port;
    private String name;

    private Socket socket;
    // 连接状态
    private volatile boolean isConn = false;
    // 最后一次发送数据时间
    private long lastSendTime;

    public Client(String ip, int port, String name) {
        this.ip = ip;
        this.port = port;
        this.name = name;
    }

    public void start(){
        if (isConn)return;
        try {
            System.out.println(name + "已启动.....");
            socket = new Socket(ip, port);
            isConn = true;
            // 保持长连接的线程，每隔2秒项服务器发一个一个保持连接的心跳消息
            lastSendTime = System.currentTimeMillis();
            new Thread(new KeepAliveWatchDog()).start();
            // 接受消息的线程，处理消息
            new Thread(new ReceiveWatchDog()).start();
        }catch (Exception e){
            e.printStackTrace();
            stop();
        }
    }

    public void stop(){
        if (isConn){
            isConn = false;
        }
    }

    class KeepAliveWatchDog implements Runnable{
        // 连接推迟时间2s
        long keepAliveDelay = 2000;
        // 检测推迟时间
        long checkDelay = 10;

        public void run() {
            if (isConn == false){
                System.out.println(isConn);
            }
            while (isConn){
                long tt = (System.currentTimeMillis() - lastSendTime);
                // 到了时间需要去发送心跳检测
                if (tt > keepAliveDelay){
                    try {
                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject(new KeepAlive(name));
                        oos.flush();
                        lastSendTime = System.currentTimeMillis();
                    }catch (Exception e){
                        e.printStackTrace();
                        stop();
                    }
                }else {
                    try {
                        TimeUnit.MILLISECONDS.sleep(checkDelay);
                    }catch (Exception e){
                        e.printStackTrace();
                        stop();
                    }
                }
            }
        }
    }


    class ReceiveWatchDog implements Runnable{
        public void run() {
            while (isConn){
                try {
                    InputStream in = socket.getInputStream();
                    // 判断流里面的字节数
                    if (in.available() > 0){
                        ObjectInputStream ois = new ObjectInputStream(in);
                        Object o = ois.readObject();
                        System.out.println(name + "check result：" + o.toString());
                    }else {
                        TimeUnit.MILLISECONDS.sleep(10);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
