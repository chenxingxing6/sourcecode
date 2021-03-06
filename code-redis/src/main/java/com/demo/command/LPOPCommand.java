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
public class LPOPCommand implements ICommand{
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 1) {
            String key = new String((byte[]) args.get(0));
            try {
                if (dbData.getList(key).isEmpty()){
                    Protocolcode.writeBulkString(out,"");
                    return;
                }
                String value=dbData.getList(key).remove(0);
                Protocolcode.writeBulkString(out,value);
                PermanentData.getInstance().clearListProfile();
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
