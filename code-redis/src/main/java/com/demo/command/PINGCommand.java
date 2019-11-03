package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 23:06
 */
public class PINGCommand implements ICommand {
    private List<Object> args;
    private DbData dbData = DbData.getDatabase();

    @Override
    public void run(OutputStream out) {
        try {
            Protocolcode.writeBulkString(out, "PONG");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
