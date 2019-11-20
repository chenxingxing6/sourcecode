package com.sort;

import cn.hutool.json.JSONUtil;

/**
 * User: lanxinghua
 * Date: 2019/11/20 18:11
 * Desc: 冒泡排序
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] nums = {1,4,9,10,3,5};
        System.out.println("排序前：" + JSONUtil.toJsonStr(nums));
        System.out.println("排序后："+ JSONUtil.toJsonStr(sort(nums)));

    }

    private static int[] sort(int[] nums){
        int times = nums.length -1;
        // 循环次数
        for (int i = 0; i < times; i++) {
            System.out.println("第" + (i+1) +"趟....");
            // 循环替换元素
            for (int j = 0; j < times - i; j++) {
                if (nums[j] > nums[j+1]){
                    System.out.println("交换：" + nums[j] + "-" + nums[j+1]);
                    int temp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = temp;
                }
            }
        }
        return nums;
    }
}
