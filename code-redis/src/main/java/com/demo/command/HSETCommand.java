package com.demo.command;

import com.demo.data.DbData;
import com.demo.data.PermanentData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:53
 */
public class HSETCommand implements ICommand {
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 3){
            String key = new String((byte[]) args.remove(0));
            String field = new String((byte[]) args.remove(0));
            String value = new String((byte[]) args.remove(0));
            HashMap<String, String> map = dbData.getDatabase().getHash(key);
            try {
                if (map.containsKey(field)) {
                    Protocolcode.writeInteger(out, 0);
                } else {
                    Protocolcode.writeInteger(out, 1);
                }
                map.put(field, value);
                PermanentData.getInstance().writetoMapProfile();
            } catch (Exception ex) {
                ex.printStackTrace();
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
