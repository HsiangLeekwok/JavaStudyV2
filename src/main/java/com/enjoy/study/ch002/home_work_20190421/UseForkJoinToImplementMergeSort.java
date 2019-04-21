package com.enjoy.study.ch002.home_work_20190421;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/21 21:04<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 利用 Fork/Join 方式实现归并排序
 * <b>Description</b>: 利用 fork/join 方式实现归并排序并比较不同数组大小的性能对比
 */
public class UseForkJoinToImplementMergeSort {

    private static final int SIZE = 40000;
    private static final int THRESHOLD = SIZE / 100;

    private static class MergeTask extends RecursiveTask<int[]> {

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
        private void sort() {
            int temp;
            for (int i = 0; i < source.length; i++) {
                for (int j = 0; j < source.length; j++) {
                    if (source[j] > source[j + 1]) {
                        temp = source[j + 1];
                        source[j + 1] = source[j];
                        source[j] = temp;
                    }
                }
            }
        }

        @Override
        protected int[] compute() {
            int len = source.length;
            if (len <= THRESHOLD) {
                sort();
                return source;
            } else {
                int mid = len / 2;
                int[] left = new int[mid];

            }
            return new int[0];
        }
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int[] array = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(SIZE);
        }
        System.out.println("init array used: " + (System.currentTimeMillis() - start) + "ms");

    }
}
