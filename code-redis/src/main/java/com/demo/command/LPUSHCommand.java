package com.demo.command;

import com.demo.data.DbData;
import com.demo.data.PermanentData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:51
 */
public class LPUSHCommand implements ICommand {
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        //接收到的是列表，列表拆解
        if (args.size() == 2) {
            String key = new String((byte[]) args.get(0));
            String value = new String((byte[]) args.get(1));
            List<String> dateList = dbData.getList(key);
            dateList.add(0, value);
            try {
                Protocolcode.writeInteger(out, dateList.size());
                PermanentData.getInstance().writetoListProfile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Protocolcode.writeError(out, "Wrong Format");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
