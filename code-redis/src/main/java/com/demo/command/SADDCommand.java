package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;
import java.util.Set;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:53
 */
public class SADDCommand implements ICommand {
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 2){
            String key = new String((byte[]) args.remove(0));
            String value = new String((byte[]) args.remove(0));
            Set<String> set = dbData.getSet(key);
            try {
                if (set.contains(value)) {
                    //0代表将原有的数据更新
                    Protocolcode.writeInteger(out, 0);
                } else {
                    //1代表插入一条语句
                    set.add(value);
                    Protocolcode.writeInteger(out, 1);
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
