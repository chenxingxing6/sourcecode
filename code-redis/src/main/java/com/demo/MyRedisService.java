package com.demo;

import com.demo.command.ICommand;
import com.demo.data.PermanentData;
import com.demo.procotol.MyDecode;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:48
 */
public class MyRedisService {

    public static void main(String[] args) {
        new MyRedisService().start(6379);
    }

    public void start(int port){
        // 1.加载持久化数据
        loadData();
        ExecutorService executor = Executors.newFixedThreadPool(20000);
        ServerSocket serverSocket = null;
        try {
            // 循环接受客户端连接
            serverSocket = new ServerSocket(port);
            while (true){
                final Socket socket = serverSocket.accept();
                executor.execute(() -> {
                    try {
                        // 持续提供业务服务
                        while (true){
                            InputStream is = socket.getInputStream();
                            OutputStream os = socket.getOutputStream();
                            // 解析命令
                            ICommand command = new MyDecode(is, os).getCommand();
                            if (command != null){
                                command.run(os);
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadData(){
        PermanentData.getInstance().readFromListProfile();
        PermanentData.getInstance().readFromMapProfile();
        System.out.println("数据加载完成....");
    }
}
