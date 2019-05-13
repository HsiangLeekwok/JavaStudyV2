package com.enjoy.study.ch006;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/13 13:05<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class DogTest {

    class Dog {
        public String name;
        public int age;
        public int step;

        public Dog(String name, int age) {
            this.name = name;
            this.age = age;
            this.step = 0;
        }

        public void run(Dog fast) {
            fast.step++;
        }
    }

    public static void main(String[] args) {
        DogTest test = new DogTest();
        Dog d = test.new Dog("Tom", 3);
        d.step = 25;
        d.run(d);
        System.out.println(d.step);
    }
}
