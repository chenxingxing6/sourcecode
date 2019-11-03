package com.demo.command;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:51
 */
public class EXITCommand implements ICommand {
    public List<Object> args;

    public void run(OutputStream out) {
        if (args.size() == 1 && ("exit".equals(args.get(0)))){
            System.exit(0);
        }
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }
}
