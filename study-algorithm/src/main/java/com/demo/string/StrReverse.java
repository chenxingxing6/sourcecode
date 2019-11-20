package com.demo.string;

import java.util.Stack;

/**
 * User: lanxinghua
 * Date: 2019/11/20 13:42
 * Desc: 字符串反转
 */
public class StrReverse {
    public static void main(String[] args) {
        String str = "abcd";

        System.out.println("递归实现："+reverse1(str));
        System.out.println("StringBuffer实现："+reverse2(str));
        System.out.println("数组实现："+reverse3(str));
        System.out.println("字符串拼接实现："+reverse4(str));
        System.out.println("栈实现："+reverse5(str));
        System.out.println("变量替换实现："+reverse6(str));
        System.out.println("异或操作实现："+reverse7(str));
    }

    // 递归实现
    private static String reverse1(String str){
        int len = str.length();
        if (len <=1){
            return str;
        }
        String left = str.substring(0, len/2);
        String right = str.substring(len/2, len);
        return reverse1(right) + reverse1(left);
    }

    // StringBuffer实现
    private static String reverse2(String str){
        StringBuffer sb = new StringBuffer(str);
        return sb.reverse().toString();
    }


    // 数组实现
    private static String reverse3(String str){
        char[] chars = str.toCharArray();
        String result = "";
        for (int i = chars.length -1; i >=0; i--) {
            result+=chars[i];
        }
        return result;
    }

    // 字符串拼接
    private static String reverse4(String str){
        String result = "";
        int len = str.length();
        for (int i = len -1; i >=0; i--) {
            result += str.charAt(i);
        }
        return result;
    }

    // 栈实现
    private static String reverse5(String str){
        // 建栈
        Stack<Character> stack = new Stack<Character>();
        for (int i = 0; i < str.length(); i++) {
            stack.push(str.charAt(i));
        }
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            result += stack.pop();
        }
        return result;
    }

    // 变量替换
    private static String reverse6(String str){
        char[] chars = str.toCharArray();
        int len = str.length();
        int midIndex = str.length()/2;
        for (int i = 0; i < midIndex; i++) {
            char temp = chars[i];
            chars[i] = chars[len - i -1];
            chars[len -i - 1] = temp;
        }
        return new String(chars);
    }

    // 位异或操作实现 A=A^B;B=A^B;A=A^B; [不同 才可以得1，否则为0]
    private static String reverse7(String str){
        char[] chars = str.toCharArray();
        int begin = 0;
        int end = str.length() -1;
        while (begin < end){
            chars[begin] = (char) (chars[begin] ^ chars[end]);
            chars[end] = (char) (chars[begin] ^ chars[end]);
            chars[begin] = (char) (chars[begin] ^ chars[end]);
            begin++;
            end--;
        }
        return new String(chars);
    }
}
