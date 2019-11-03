package com.demo.procotol;

import com.demo.command.ICommand;
import com.demo.exception.RedisException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 20:05
 */
public class MyDecode {
    private InputStream is;
    private OutputStream os;
    private ProtocolInputStream pis;

    public MyDecode(InputStream is, OutputStream os) {
        this.is = is;
        this.os = os;
        pis = new ProtocolInputStream(is);
    }

    public ICommand getCommand(){
        try {
            Object o = process();
            if (!(o instanceof List)){
                Protocolcode.writeBulkString(os, "Server too tired,please wait .....");
                throw new RedisException("内部解析错误，服务器故障");
            }
            List<Object> list = (List<Object>) o;
            if (list.size() < 1) {
                Protocolcode.writeBulkString(os, "Server too tired,please wait .....");
                throw new RedisException("内部解析错误，服务器故障");
            }
            Object o2 = list.remove(0);
            if (!(o2 instanceof byte[])) {
                Protocolcode.writeBulkString(os, "Server too tired,please wait .....");
                throw new RedisException("内部解析错误，服务器故障");
            }
            String commandName = String.format("%sCommand", new String((byte[]) (o2)).trim().toUpperCase());
            System.out.println("执行命令：" + commandName);
            Class<?> cls = null;
            ICommand command = null;
            cls = Class.forName("com.demo.command." + commandName);
            if (cls == null || !ICommand.class.isAssignableFrom(cls)){
                Protocolcode.writeError(os, "Wrong Input,Please try again");
            }else {
                command = (ICommand) cls.newInstance();
            }
            command.setArgs(list);
            return command;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String processError() throws IOException {
        return pis.readLine();
    }

    public String processSimpleString() throws IOException {
        return pis.readLine();
    }

    public long processInteger() throws IOException {
        return pis.readInteger();
    }

    //'$6\r\nfoobar\r\n' 或者 '$-1\r\n'
    public byte[] processBulkString() {
        int len = 0;
        byte[] bytes;
        String str = null;
        try {
            len = (int) pis.readInteger();
            bytes = new byte[len];
            str = pis.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.getBytes();
    }

    public List<byte[]> processArray() {
        int len = 0;
        List<byte[]> list = new ArrayList<byte[]>();
        try {
            len = (int) pis.readInteger();
            //"*5\r\n
            // 5\r\nlpush\r\n$3\r\nkey\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n";
            for (int i = 0; i < len; i++) {
                byte[] bytes = (byte[]) process();
                list.add(bytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Object process() throws IOException {
        int b = 0;
        try {
            b = is.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (b == -1) {
            throw new RuntimeException("程序错误..........");
        }
        switch ((char) b) {
            case '+':
                return processSimpleString();
            case '-':
                return processError();
            case ':':
                return processInteger();
            case '$':
                return processBulkString();
            case '*':
                return processArray();
            default:
                Protocolcode.writeError(os, "Unresolve this commond");
        }
        return null;
    }
}
