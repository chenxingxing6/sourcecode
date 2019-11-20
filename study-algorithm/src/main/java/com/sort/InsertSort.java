package com.sort;

import cn.hutool.json.JSONUtil;

/**
 * User: lanxinghua
 * Date: 2019/11/20 19:27
 * Desc: 插入排序
 */
public class InsertSort {

    public static void main(String[] args) {
        int[] nums = {1,4,9,10,3,5};
        System.out.println("排序前：" + JSONUtil.toJsonStr(nums));
        System.out.println("排序后："+ JSONUtil.toJsonStr(sort(nums)));
    }

    private static int[] sort(int[] nums){
        // 对下标为1元素进行插入，下标为0元素默认是有序的
        for (int i = 1; i < nums.length; i++) {
            int currValue = nums[i];
            int position = i;
            // 对已排序的元素从右往左扫描
            for (int j = i - 1; j >= 0; j--){
                if (nums[j] > currValue){
                    // 未排序值确定，插入位置向前移动
                    nums[j+1] = nums[j];
                    position -=1;
                }else {
                    break;
                }
            }

            // 值插入的位置
            nums[position] = currValue;
        }
        return nums;
    }
}
