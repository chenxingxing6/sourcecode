package com.demo.longconn.server;

import com.demo.longconn.KeepAlive;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: lanxinghua
 * Date: 2019/11/4 18:49
 * Desc: 服务端,服务端检测receiveTimeDelay内没接收到任何数据，自动和客户端断开连接。
 */
public class Server {
    private int port;

    private ServerSocket serverSocket;
    private Socket socket;
    // 连接状态
    private volatile boolean isConn = false;
    // 检测心跳时间 3s
    private long receiveTimeDelay = 3000;
    // 最后一次接收时间
    long lastReceiveTime;
    // 有效客户端
    private static Map<String/*clientName*/, ClientState> map = new HashMap<String, ClientState>(10);
    private Object lock = new Object();

    class ClientState{
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


    public Server(int port) {
        this.port = port;
    }

    public void start(){
        if (isConn) return;
        try {
            System.out.println("服务端启动......");
            isConn = true;
            // 检测客户端心跳
            new Thread(new ConnectWatchDog()).start();
        }catch (Exception e){
            e.printStackTrace();
            stop();
        }

    }

    public void stop(){
        if (isConn) isConn = false;
    }

    class ConnectWatchDog implements Runnable{
        public void run() {
            try {
                serverSocket = new ServerSocket(port);
                while (isConn){
                    socket = serverSocket.accept();
                    lastReceiveTime = System.currentTimeMillis();
                    new Thread(new SocketAction(socket)).start();
                }
            }catch (Exception e){
                e.printStackTrace();
                stop();
            }
        }
    }

    class SocketAction implements Runnable{
        boolean isRun = true;
        Socket s;
        ClientState clientState;

        public SocketAction(Socket s) {
            this.s = s;
            clientState = new ClientState();
        }

        public void run() {
            try {
                while (isConn && isRun){
                    if (System.currentTimeMillis() - lastReceiveTime > receiveTimeDelay){
                        new Thread(new CleanScan()).start();
                        close();
                    }else {
                        InputStream in = s.getInputStream();
                        if (in.available() > 0){
                            ObjectInputStream ois = new ObjectInputStream(in);
                            Object o = ois.readObject();
                            lastReceiveTime = System.currentTimeMillis();
                            if (o instanceof KeepAlive){
                                KeepAlive alive = (KeepAlive) o;
                                System.out.println(alive.getClientName() + "心跳检查ok：" + o.toString());
                                clientState.setClientName(alive.getClientName());
                                clientState.setIsValid(1);
                                clientState.setLastReTime(lastReceiveTime);
                                map.put(alive.getClientName(), clientState);
                            }
                            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                            oos.writeObject("ok");
                            oos.flush();
                        }else {
                            TimeUnit.MILLISECONDS.sleep(10);
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                close();
            }
        }

        // 清除宕机的机器
        class CleanScan implements Runnable{
            public void run() {
                synchronized (lock){
                    if (map.isEmpty()){
                        return;
                    }
                    for (Map.Entry<String, ClientState> client : map.entrySet()) {
                        ClientState clientState = client.getValue();
                        if (clientState == null){
                            map.remove(client.getKey());
                            return;
                        }
                        if (clientState.getIsValid() == 0){
                            continue;
                        }
                        if (System.currentTimeMillis() - clientState.getLastReTime() > receiveTimeDelay){
                            System.out.println("客户端宕机" + clientState.toString());
                            clientState.setIsValid(0);
                        }
                    }
                }
            }
        }

        private void close(){
            if (isRun) isRun = false;
            if (socket!=null){
                try {
                    socket.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }


}
