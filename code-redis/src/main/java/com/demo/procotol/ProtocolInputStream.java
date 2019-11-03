package com.demo.procotol;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: cxx
 * @Date: 2019/11/3 20:09
 */
public class ProtocolInputStream {
    private InputStream is;

    public ProtocolInputStream(InputStream is) {
        this.is = is;
    }

    public long readInteger() throws IOException {
        int b = is.read();
        if (b == -1) {
            throw new RuntimeException("不应该读到文件末尾");
        }
        StringBuilder sb = new StringBuilder();
        boolean NegativeFlag = false;
        if (b == '-') {
            NegativeFlag = true;
        } else {
            sb.append((char) b);
        }
        while (true) {
            b = is.read();
            if (b == -1) {
                throw new RuntimeException("不应该读到文件末尾");
            }
            if (b == '\r') {
                int c = is.read();
                if (c == '\n') {
                    break;
                } else {
                    throw new RuntimeException("指令读取失败");
                }
            } else {
                sb.append((char) (b));
            }
        }
        long rs = Long.parseLong(sb.toString());
        if (NegativeFlag) {
            rs = -rs;
        }
        return rs;
    }

    public String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean flag = true;
        int b = 0;
        while (true) {
            if (flag) {
                b = is.read();
            } else {
                flag = true;
            }
            if (b == -1) {
                throw new RuntimeException("不应该读到文件末尾");

            }
            if (b == '\r') {
                int c = is.read();
                if (c == -1) {
                    throw new RuntimeException("不应该读到文件末尾");
                } else if (c == '\n') {
                    break;
                } else if (c == '\r') {
                    sb.append((char) (b));
                    b = c;
                    flag = false;
                } else {
                    sb.append((char) c);
                }
            }
            sb.append((char) b);
        }
        return sb.toString();
    }
}
