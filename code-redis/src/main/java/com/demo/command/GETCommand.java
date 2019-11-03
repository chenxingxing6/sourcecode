package com.demo.command;

import com.demo.data.DbData;
import com.demo.data.PermanentData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 22:30
 */
public class GETCommand implements ICommand {
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 1){
            String key = new String((byte[]) args.remove(0));
            try {
                String value = dbData.str.remove(key);
                Protocolcode.writeBulkString(out,value);
                // TODO: 2019/11/3  持久化到文件里
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            try {
                Protocolcode.writeError(out, "Wrong Format");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
