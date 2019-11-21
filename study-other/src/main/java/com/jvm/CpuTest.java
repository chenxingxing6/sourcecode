package com.jvm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: lanxinghua
 * Date: 2019/11/21 18:14
 * Desc:
 */
public class CpuTest {
    public static void main(String[] args) {
        List<Map> list = new ArrayList<>();
        while (true){
            HashMap<String, Object> map = new HashMap<>();
            map.put("key", new Object());
            list.add(map);
            if (list.size() > 3000000){
                System.out.println("clean");
                list.clear();
            }
        }
    }
}
