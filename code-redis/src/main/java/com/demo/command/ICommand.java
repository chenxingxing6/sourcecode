package com.demo.command;

import java.io.OutputStream;
import java.util.List;

/**
 * @Author: cxx
 * @Date: 2019/11/3 19:49
 */
public interface ICommand {
    public void run(OutputStream out);

    public void setArgs(List<Object> args);
}
