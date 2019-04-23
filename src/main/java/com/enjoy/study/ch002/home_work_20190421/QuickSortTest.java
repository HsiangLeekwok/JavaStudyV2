package com.enjoy.study.ch002.home_work_20190421;

import java.util.Arrays;
import java.util.Random;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/04/23 22:23<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class QuickSortTest {

    private static final int SIZE = 10000000;
    private static int[] source;

    private static void quickSort(int left, int right) {
        if (left >= right) return;

        int base = source[left];
        int low = left, high = right;
        while (low != high) {
            // 从右往左查看比base小的第一个数字
            while (low < high && base <= source[high]) {
                high--;
            }
            // 找到第一个比base小的数
            //if (high > low) {
            source[low] = source[high];
            //System.out.println("source: " + Arrays.toString(source));
            //}
            // 从左到右查看比base大的第一个数字
            while (low < high && base >= source[low]) {
                low++;
            }
            //if (low < high) {
            source[high] = source[low];
            //System.out.println("source: " + Arrays.toString(source));
            //}
        }
        source[low] = base;

        //System.out.println("source: " + Arrays.toString(source));
        quickSort(left, low - 1);
        quickSort(low + 1, right);
    }

    static void sort(int arr[], int low, int high) {
        int l = low;
        int h = high;
        int povit = arr[low];

        while (l < h) {
            while (l < h && arr[h] >= povit)
                h--;
            if (l < h) {
                arr[l] = arr[h];
                l++;
            }

            while (l < h && arr[l] <= povit)
                l++;

            if (l < h) {
                arr[h] = arr[l];
                h--;
            }
        }
        arr[l] = povit;
        //print(arr);
        //System.out.print("l=" + (l + 1) + "h=" + (h + 1) + "povit=" + povit + "\n");
        if (l - 1 > low) sort(arr, low, l - 1);
        if (h + 1 < high) sort(arr, h + 1, high);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        source = new int[SIZE];
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            source[i] = random.nextInt(100);
        }
        //System.out.println("source: " + Arrays.toString(source));
        System.out.println("init used: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        quickSort(0, SIZE - 1);
        //sort(source, 0, SIZE - 1);
        //System.out.println("source: " + Arrays.toString(source));
        System.out.println("sort used: " + (System.currentTimeMillis() - start) + "ms");
    }
}
