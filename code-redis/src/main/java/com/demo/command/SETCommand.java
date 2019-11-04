package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 22:30
 */
public class SETCommand implements ICommand {
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 2){
            String key = new String((byte[]) args.remove(0));
            String value = new String((byte[]) args.remove(0));
            String v = dbData.str.get(key);
            dbData.str.put(key, value);
            try {
                if (v == null){
                    Protocolcode.writeInteger(out, 1L);
                }else {
                    Protocolcode.writeInteger(out, 0L);
                }
            }catch (Exception e){
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
