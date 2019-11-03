package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: cxx
 * @Date: 2019/11/3 22:41
 */
public class SCARDCommand implements ICommand{
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 1){
            String key = new String((byte[]) args.remove(0));
            Set<String> set = dbData.getSet(key);
            try {
                if (set.isEmpty()) {
                    Protocolcode.writeArray(out, new ArrayList<>());
                } else {
                    Protocolcode.writeArray(out, new ArrayList<>(set));
                }
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
