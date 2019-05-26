package com.enjoy.study.season01_Concurrent.ch002.home_work_20190421;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/04/21 21:04<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 利用 Fork/Join 方式实现归并排序
 * <b>Description</b>: 利用 fork/join 方式实现归并排序并比较不同数组大小的性能对比
 */
public class UseForkJoinToImplementMergeSort {

    private static final int SIZE = 1000000;
    private static final int THRESHOLD = 10;

    private static class MergeTask extends RecursiveTask<int[]> {

        private int[] source;
        private boolean useQuick;

        MergeTask(int[] array, boolean quick) {
            this.source = array;
            this.useQuick = quick;
        }

        // 快速排序
        private void quickSort() {
            int start = 0, end = source.length - 1;
            quickSort(start, end);
        }

        private void quickSort(int left, int right) {

            if (left >= right) return;

            int base = source[left];
            int low = left, high = right;
            while (low != high) {
                // 从右往左查看比base小的第一个数字
                while (low < high && base <= source[high]) {
                    high--;
                }
                // 找到第一个比base小的数
                source[low] = source[high];
                // 从左到右查看比base大的第一个数字
                while (low < high && base >= source[low]) {
                    low++;
                }
                source[high] = source[low];
            }
            source[low] = base;

            quickSort(left, low - 1);
            quickSort(low + 1, right);
        }

        // 插入排序：借用老师的
        public static int[] sort(int[] array) {
            if (array.length == 0)
                return array;
            int currentValue;/*当前待排序数据，该元素之前的元素均已被排序过*/
            for (int i = 0; i < array.length - 1; i++) {
                int preIndex = i;/*已被排序数据的索引*/
                currentValue = array[preIndex + 1];

            /*在已被排序过数据中倒序寻找合适的位置，如果当前待排序数据比比较的元素要小，
            将比较的元素元素后移一位*/
                while (preIndex >= 0 && currentValue < array[preIndex]) {
                    //将当前元素后移一位
                    array[preIndex + 1] = array[preIndex];
                    preIndex--;
                }
                /*while循环结束时，说明已经找到了当前待排序数据的合适位置，插入*/
                array[preIndex + 1] = currentValue;
            }
            return array;
        }

        // 合并：借用老师的方法
        private int[] merge(int[] left, int[] right) {
            int[] result = new int[left.length + right.length];
            for (int index = 0, i = 0, j = 0; index < result.length; index++) {
                if (i >= left.length)/*左边数组已经取完，完全取右边数组的值即可*/
                    result[index] = right[j++];
                else if (j >= right.length)/*右边数组已经取完，完全取左边数组的值即可*/
                    result[index] = left[i++];
                else if (left[i] > right[j])/*左边数组的元素值大于右边数组，取右边数组的值*/
                    result[index] = right[j++];
                else/*右边数组的元素值大于左边数组，取左边数组的值*/
                    result[index] = left[i++];
            }

            return result;
        }

        @Override
        protected int[] compute() {
            if (source.length <= THRESHOLD) {
                if (useQuick) {
                    quickSort();
                    return source;
                } else {
                    return sort(source);
                }
            } else {
                int mid = source.length / 2;
                MergeTask leftTask = new MergeTask(Arrays.copyOfRange(source, 0, mid), useQuick);
                MergeTask rightTask = new MergeTask(Arrays.copyOfRange(source, mid + 1, source.length), useQuick);
                invokeAll(leftTask, rightTask);
                int[] left = leftTask.join();
                int[] right = rightTask.join();
                return merge(left, right);
            }
        }
    }


    public static void main(String[] args) {
        int[] array = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100);
        }
        long start = System.currentTimeMillis();
        System.out.println("array length: " + SIZE);

        ForkJoinPool pool = new ForkJoinPool();
        MergeTask task = new MergeTask(array, true);
        pool.invoke(task);
        System.out.println("quick sort used time: " + (System.currentTimeMillis() - start) + "ms");
        task = new MergeTask(array, false);
        pool.invoke(task);
        System.out.println("insertion sort used time: " + (System.currentTimeMillis() - start) + "ms");
        //System.out.println("task running.......");
        //int[] ret = task.join();
        //System.out.println(Arrays.toString(ret));
    }
}
