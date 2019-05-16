package com.enjoy.study.ch008.edu.internal;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/16 22:20<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/> 延时队列的节点包装类
 * <b>Description</b>:
 */
public class DelayItem<Data> implements Delayed {

    private long activeTime;
    private Data data;

    public DelayItem(long expirationTime, Data data) {
        this.activeTime = expirationTime * 1000 + System.currentTimeMillis();
    }

    public long getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(long activeTime) {
        this.activeTime = activeTime;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(activeTime - System.currentTimeMillis(), unit);
    }

    @Override
    public int compareTo(Delayed o) {
        long d = o.getDelay(TimeUnit.MILLISECONDS) - getDelay(TimeUnit.MILLISECONDS);
        return d > 0 ? 1 : (d < 0 ? -1 : 0);
    }
}
