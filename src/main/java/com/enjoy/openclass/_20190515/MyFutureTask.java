package com.enjoy.openclass._20190515;

import java.util.concurrent.*;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/15 20:56<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/> 自定义FutureTask
 * <b>Description</b>:
 */
public class MyFutureTask<V> implements Runnable, Future<V> {

    Callable<V> callable;

    public MyFutureTask(Callable<V> callable) {
        this.callable = callable;
    }

    private V result;

    @Override
    public void run() {
        try {
            result = callable.call();
            synchronized (this) {
                this.notifyAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        if (null != result) {
            return result;
        }
        synchronized (this) {
            this.wait();
        }
        return result;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
