package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:53
 */
public class HGETCommand implements ICommand{
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 2){
            String key = new String((byte[]) args.remove(0));
            String field = new String((byte[]) args.remove(0));
            HashMap<String, String> map = dbData.getDatabase().getHash(key);
            String value = map.get(field);
            try {
                Protocolcode.writeBulkString(out, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
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
