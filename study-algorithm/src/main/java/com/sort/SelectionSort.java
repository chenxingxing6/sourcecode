package com.sort;

import cn.hutool.json.JSONUtil;

/**
 * User: lanxinghua
 * Date: 2019/11/20 18:32
 * Desc: 选择排序
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] nums = {1,4,9,10,3,5};
        System.out.println("排序前：" + JSONUtil.toJsonStr(nums));
        System.out.println("排序后："+ JSONUtil.toJsonStr(sort(nums)));

    }

    private static int[] sort(int[] nums){
        // N-1轮比较
        for (int i = 0; i < nums.length -1 ; i++) {
            int min = i;
            // 每轮需要N-1次比较
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[min]){
                    // 找到最小坐标
                    min = j;
                }
            }

            // 判断最小值是否有改动，进行替换
            if (min != i){
                int temp = nums[i];
                nums[i] = nums[min];
                nums[min] = temp;
            }
        }
        return nums;
    }
}
