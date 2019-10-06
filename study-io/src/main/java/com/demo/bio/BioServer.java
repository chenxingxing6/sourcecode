package com.demo.bio;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author: cxx
 * @Date: 2019/10/6 11:26
 */
public class BioServer {
    private int port;
    private Socket client;

    public BioServer(int port) {
        this.port = port;
    }

    public void listener(){
        try {
            System.out.println("服务端开启,等待连接.....");
            ServerSocket socket = new ServerSocket(port);
            client = socket.accept();

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            Object result = ois.readObject();
            System.out.println("接收信息：" + result.toString());

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeUTF("服务器发送消息,欢迎你");
            oos.flush();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (client != null){
                try {
                    client.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new BioServer(8080).listener();
    }
}
