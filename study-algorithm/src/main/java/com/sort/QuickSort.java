package com.sort;

import cn.hutool.json.JSONUtil;

/**
 * User: lanxinghua
 * Date: 2019/11/20 20:13
 * Desc: 快速排序
 * 排序前：[1,4,9,10,3,5]
 * 基数定为：1
 * 分界点位置：0  分界点值：1
 *
 * 基数定为：4
 * 分界点位置：2  分界点值：9
 *
 * 基数定为：10
 * 分界点位置：5  分界点值：5
 *
 * 基数定为：5
 * 分界点位置：3  分界点值：5
 *
 * 排序后：[1,3,4,5,9,10]
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] nums = {1,4,9,10,3,5};
        System.out.println("排序前：" + JSONUtil.toJsonStr(nums));
        quickSort(nums, 0, nums.length -1);
        System.out.println("排序后："+ JSONUtil.toJsonStr(nums));
    }

    // 假设从小到大
    private static int[] quickSort(int[] nums, int low, int high){
        if (low < high){
            int part = partition(nums, low, high);
            // 递归调用
            quickSort(nums, low, part -1);
            quickSort(nums, part+1, high);
        }

        return nums;
    }

    // 切分方法
    private static int partition(int[] nums, int low, int high){
        // 先基准数
        int base = nums[low];
        System.out.println("基数定为：" + base);
        // 两端交替向内扫描
        while (low < high){
            while (low < high && nums[high] >= base){
                high--;
            }
            // 将比基数小的数向低端移
            nums[low] = nums[high];

            while (low < high && nums[low] <= base){
                low++;
            }
            // 将比基数大的数向高端移
            nums[high] = nums[low];
        }
        // 设置基数
        System.out.println("分界点位置：" + low  + "  分界点值：" + nums[low]);
        System.out.println();
        nums[low] = base;
        return low;
    }
}
