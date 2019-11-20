package com.demo.string;

/**
 * User: lanxinghua
 * Date: 2019/11/20 14:51
 * Desc: 用于删除与指定字符串匹配的子串
 */
public class DelStr {
    public static void main(String[] args) {
        String target = "ababdabcxxcdabcabc";
        String pattern = "cxx";
        System.out.println(deleteAll(target, pattern));
    }

    // 返回将target串中所有与pattern匹配的子串删除后的字符串
    public static String deleteAll(String target, String pattern) {
        int i = target.indexOf(pattern);
        while (i != -1) {
            target = target.substring(0, i)
                    + target.substring(i + pattern.length());
            i = target.indexOf(pattern, i);
        }
        return target;
    }
}
