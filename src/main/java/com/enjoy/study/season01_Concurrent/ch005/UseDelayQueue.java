package com.enjoy.study.season01_Concurrent.ch005;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/09 20:45<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class UseDelayQueue {

    public class DelayItem<T> implements Delayed {
        private long activeTime;
        private T data;

        public DelayItem(long expirationTime, T data) {
            this.activeTime = expirationTime * 1000 + System.currentTimeMillis();
        }

        public long getActiveTime() {
            return activeTime;
        }

        public void setActiveTime(long activeTime) {
            this.activeTime = activeTime;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long d = unit.convert(activeTime - System.currentTimeMillis(), unit);
            return d;
        }

        @Override
        public int compareTo(Delayed o) {
            return 0;
        }
    }

}
