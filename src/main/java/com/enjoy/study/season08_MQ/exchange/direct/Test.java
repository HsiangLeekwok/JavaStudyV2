package com.enjoy.study.season08_MQ.exchange.direct;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/19 21:33<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class Test {

    public static void main(String[] args) {
        int size = 999999;
        // b43d6151-08b4-4dfa-a915-f6e0f78debc1
        String uuid = UUID.randomUUID().toString();
//        System.out.println(uuid);
//        uuid = uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) +
//                uuid.substring(19, 23) + uuid.substring(24);
//        System.out.println(uuid);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(UUID.randomUUID().toString());
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            uuid = replaceDash(list.get(i));
        }
        System.out.println("function replace used time: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            uuid = substringDash(list.get(i));
        }
        System.out.println("function substring used time: " + (System.currentTimeMillis() - start));
    }

    private static String replaceDash(String uuid) {
        return uuid.replace("-", "");
    }

    private static String substringDash(String uuid) {
        return uuid.substring(0, 8) + uuid.substring(9, 13) + uuid.substring(14, 18) +
                uuid.substring(19, 23) + uuid.substring(24);
    }
}
