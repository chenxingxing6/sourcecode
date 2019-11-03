package com.demo.data;

import java.util.*;

/**
 * @Author: cxx
 * @Date: 2019/11/3 20:40
 */
public class DbData {
    private static DbData dbData = new DbData();
    private DbData() {
    }

    public static DbData getDatabase() {
        return dbData;
    }

    public static Map<String, String> str = new HashMap<>();
    public static Map<String, HashMap<String, String>> hash = new HashMap<>();
    public static Map<String, List<String>> list = new HashMap<>();
    public static Map<String, Set<String>> set = new HashMap<>();
    public static Map<String, LinkedList<String>> zset = new HashMap<>();

    public static List<String> getList(String key) {
        List<String> lists = list.computeIfAbsent(key, k -> {
            return new ArrayList<>();
        });
        return lists;
    }

    public static Set<String> getSet(String key) {
        Set<String> hashSet = set.get(key);
        if (hashSet == null) {
            hashSet = new HashSet<>();
            set.put(key, hashSet);
            return hashSet;
        }
        return hashSet;
    }

    public static HashMap<String, String> getHash(String key) {
        HashMap<String, String> RMap = hash.get(key);
        if (RMap == null) {
            //必须创建一个列表，否则返回的是NPE，程序无法进行
            RMap = new HashMap<>();
            hash.put(key, RMap);
            return RMap;
        }
        return RMap;
    }
}
