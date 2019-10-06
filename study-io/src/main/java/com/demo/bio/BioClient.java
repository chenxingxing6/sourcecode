package com.demo.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author: cxx
 * @Date: 2019/10/6 11:25
 */
public class BioClient {
    private String host;
    private int port;
    private Socket socket;

    public BioClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void listener(){
        try {
            socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("客户端向服务器发送信息");
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Object result = ois.readUTF();
            System.out.println("服务器响应：" + result.toString());
            ois.close();
            oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new BioClient("localhost", 8080).listener();
    }
}
