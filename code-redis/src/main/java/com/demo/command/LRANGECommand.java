package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:52
 */
public class LRANGECommand implements ICommand{
    DbData dbData = DbData.getDatabase();
    List<Object> args;

    @Override
    public void run(OutputStream out) {
        if (args.size() == 3) {
            String key = new String((byte[]) args.remove(0));
            List<String> dataBase = dbData.getDatabase().getList(key);
            int start = Integer.parseInt(new String((byte[]) args.remove(0)));
            int end = Integer.parseInt(new String((byte[]) args.remove(0)));
            List<String> list=new ArrayList<>();
            try {
                if (start >= 0 && start < dataBase.size()) {
                    if (end < 0) {
                        end = dataBase.size() + end;
                        for(int i=end;i<dataBase.size();i++){
                            list.add(dataBase.get(i));
                        }
                        Protocolcode.writeArray(out, list);
                    } else {
                        if (end >= dataBase.size()) {
                            Protocolcode.writeArray(out, dataBase);
                        } else {
                            for(int i=start;i<end+1;i++){
                                list.add(dataBase.get(i));
                            }
                            Protocolcode.writeArray(out, list);
                        }
                    }
                } else {
                    for(int i=dataBase.size()+start;i<dataBase.size();i++){
                        list.add(dataBase.get(i));
                    }
                    Protocolcode.writeArray(out, dataBase.subList(dataBase.size() + start, dataBase.size()));
                }
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
