package com.demo.procotol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 20:21
 */
public class Protocolcode {
    public static void writeInteger(OutputStream write, long length) throws Exception {
        write.write(':');
        write.write(String.valueOf(length).getBytes());
        write.write("\r\n".getBytes());
        write.flush();

    }

    //'+OK\r\n'
    public static void writeString(OutputStream write, String str) throws Exception {
        write.write('+');
        write.write("OK".getBytes());
        write.write("\r\n".getBytes());
        write.flush();
    }

    //字节流'$6\r\nfoobar\r\n' 或者 '$-1\r\n'
    public static void writeBulkString(OutputStream write, String str) throws Exception {
        byte[] b = new byte[0];
        if (str != null){
            b = str.getBytes();
        }
        write.write('$');
        write.write(String.valueOf(b.length).getBytes());
        write.write("\r\n".getBytes());
        write.write(b);
        write.write("\r\n".getBytes());
        write.flush();
    }

    //'2\r\n$3\r\nfoo\r\n$3\r\nbar\r\n'
    public static void writeArray(OutputStream write, List<?> list) throws Exception {
        write.write('*');
        write.write(String.valueOf(list.size()).getBytes());
        write.write("\r\n".getBytes());
        for (Object o : list) {
            if ((o instanceof String)) {
                writeBulkString(write, (String) o);
            } else if (o instanceof Integer) {
                writeInteger(write, (long) o);
            } else if (o instanceof Long) {
                writeInteger(write, (Long) o);
            } else if (o instanceof List<?>) {
                writeArray(write, (List<?>) o);
            }
        }
    }

    //'-WRONGTYPE Operation against a key holding the wrong kind of value'
    public static void writeError(OutputStream write, String message) throws IOException {
        write.write('-');
        write.write(message.getBytes());
        write.write("\r\n".getBytes());
    }
}
