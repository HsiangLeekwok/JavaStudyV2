package com.enjoy.study.ch007;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 20:33<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/> 仿Collections.synchronizedList 方式对 Final 类的对外安全发布
 * <b>Description</b>:
 */
public class SavePublishFinalUser {

    private final SyncFinalUser user;

    public SavePublishFinalUser(FinalUser user) {
        this.user = new SyncFinalUser(user);
    }

    public static class SyncFinalUser {
        private final FinalUser user;
        private final Object lock = new Object();

        public SyncFinalUser(FinalUser user) {
            this.user = user;
        }

        public int getAge() {
            synchronized (lock) {
                return user.getAge();
            }
        }

        public void setAge(int age) {
            synchronized (lock) {
                user.setAge(age);
            }
        }
    }
}
