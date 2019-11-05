package com.demo.command;

import com.demo.data.DbData;
import com.demo.procotol.Protocolcode;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 23:06
 */
public class SELECTCommand implements ICommand {
    private List<Object> args;
    private DbData dbData = DbData.getDatabase();

    @Override
    public void run(OutputStream out) {
        try {
            if (args.size() == 1){
                String index = new String((byte[]) args.get(0));
                if (index.equalsIgnoreCase("0")){
                    Protocolcode.writeString(out, "OK");
                }
            }else {
                try {
                    Protocolcode.writeError(out, "Wrong Format");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
