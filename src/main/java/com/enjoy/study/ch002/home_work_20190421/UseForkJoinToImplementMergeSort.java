package com.enjoy.study.ch002.home_work_20190421;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/21 21:04<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 利用 Fork/Join 方式实现归并排序
 * <b>Description</b>: 利用 fork/join 方式实现归并排序并比较不同数组大小的性能对比
 */
public class UseForkJoinToImplementMergeSort {

    private static final int SIZE = 10;
    private static final int THRESHOLD = SIZE / 5;

    private static class MergeTask extends RecursiveTask<Long> {

        private int[] source;
        private int start, end;

        public MergeTask(int[] array, int startIndex, int endIndex) {
            this.source = array;
            this.start = startIndex;
            this.end = endIndex;
        }

        /**
         * 达到最小任务需求时，使用冒泡排序法进行由小到大排序
         */
        private void bubbleSort() {
            int temp;
            for (int i = start; i <= end; i++) {
                for (int j = start; j <= end; j++) {
                    if (source[j] > source[j + 1]) {
                        temp = source[j + 1];
                        source[j + 1] = source[j];
                        source[j] = temp;
                    }
                }
            }
        }

        private void quickSort() {
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

        @Override
        protected Long compute() {
            int len = end - start;
            if (len < THRESHOLD) {
                quickSort();
                return 0L;
            } else {
                int mid = len / 2;
                MergeTask left = new MergeTask(source, start, mid);
                MergeTask right = new MergeTask(source, mid + 1, end);
                invokeAll(left, right);
                return left.join() + right.join();
            }
        }
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int[] array = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(SIZE);
        }
        start = System.currentTimeMillis();

        ForkJoinPool pool = new ForkJoinPool();
        MergeTask task = new MergeTask(array, 0, SIZE - 1);
        pool.invoke(task);
        System.out.println("task running.......");
        task.join();
        System.out.println("time used: " + (System.currentTimeMillis() - start) + "ms");
    }
}
