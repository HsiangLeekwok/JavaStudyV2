package com.enjoy.study.ch005;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/07 19:06<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class BitTest {

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        int j, max = Integer.MAX_VALUE;// >> 4;
        for (int i = 0; i < max; i++) {
            j = i % 9;
        }
        System.out.println(max + " 次 mod 耗时: " + (System.currentTimeMillis() - start) + "ms");
        start = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
            j = i & (9 - 1);
        }
        System.out.println(max + " 次位运算耗时e: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Test
    public void test01() {
        System.out.println("the 4 is: " + Integer.toBinaryString(4));
        System.out.println("the 6 is: " + Integer.toBinaryString(6));
        System.out.println("the 4&6 is: " + Integer.toBinaryString(4 & 6));
        System.out.println("the 4|6 is: " + Integer.toBinaryString(4 | 6));
        System.out.println("the ~4 is: " + Integer.toBinaryString(~4));
        System.out.println("the 4^6 is: " + Integer.toBinaryString(4 ^ 6));
        System.out.println("the 4>>1 is: " + Integer.toBinaryString(4 >> 1));
        System.out.println("the 4<<1 is: " + Integer.toBinaryString(4 << 1));
        System.out.println("the 234567 is: " + Integer.toBinaryString(234567));
        System.out.println("the 234567>>>4 is: " + Integer.toBinaryString(234567 >>> 4));
        System.out.println("the -4 is: " + Integer.toBinaryString(-4));
        System.out.println("the -4>>>4 is: " + Integer.toBinaryString(-4 >>> 4));

        int j;
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            j = random.nextInt(1000);
            System.out.println(j + " is odd: " + ((j & 1) == 1));
        }
    }
}
