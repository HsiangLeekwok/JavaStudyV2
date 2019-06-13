package com.enjoy.study.season02_JVM.ch05;

import org.junit.Test;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/10 14:28<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class InterviewTest {

    class People {
        public String name;

        public People() {
            System.out.println(1);
        }

        public People(String name) {
            System.out.println(2);
            this.name = name;
        }
    }

    class Child extends People {
        People father;

        public Child(String name) {
            System.out.println(3);
            this.name = name;
            father = new People(name + ":F");
        }

        public Child() {
            System.out.println(4);
        }
    }

    @Test
    public void test() {
        new Child("mike");
    }
}
