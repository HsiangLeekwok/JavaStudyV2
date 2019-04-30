package com.enjoy.study.ch004;

import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/04/30 08:13<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/> 用 CAS 实现 FutureTask，允许多个线程 get 结果。
 * <b>Description</b>: 实现一个 FutureTask，仅实现 get 方法即可。实现方式不限，wait/notify，Lock/Condition，都可以，但以用AQS实现为佳
 */
public class HomeWork20190430_CAS_FutureTask<V> implements RunnableFuture<V> {

    /**
     * 模板类实现 AQS，单个线程写（也即执行futureTask本身的线程写结果）多个线程读
     */
    private final class Sync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean isHeldExclusively() {
            // 是否是当前线程的独占锁
            return Thread.currentThread() == getExclusiveOwnerThread();
        }

        @Override
        protected boolean tryAcquire(int arg) {
            int s = getState();
            if (s == 0) {
                if (compareAndSetState(0, arg)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (isHeldExclusively()) {
                // 释放锁
                if (compareAndSetState(1, arg)) {
                    setExclusiveOwnerThread(null);
                    return true;
                }
            }
            return false;
        }

        public void runSafety() {
            if (compareAndSetState(NEW, COMPLETING)) {
                state = COMPLETING;
                try {
                    try {
                        result = callable.call();
                    } catch (Exception e) {
                        e.printStackTrace();
                        state = EXCEPTION;
                        result = null;
                    }
                } finally {
                    state = COMPLETED;
                }
            }
        }
        public V getSafety(){
            if(compareAndSetState())
        }
    }

    // 新创建的 FucureTask 状态为 0 ………… 等待执行
    private static final int NEW = 0;
    // 正在执行过程中
    private static final int COMPLETING = 1;
    // 已经执行完毕
    private static final int COMPLETED = 2;
    // 执行过程中发生异常
    private static final int EXCEPTION = 3;

    // FutureTask 的执行状态
    private volatile int state;

    private Callable<V> callable;

    private Object result;
    // AQS 同步
    private Sync sync = new Sync();

    public HomeWork20190430_CAS_FutureTask(Callable<V> callable) {
        if (null == callable) throw new NullPointerException();
        this.callable = callable;
        // 初始化状态为等待执行状态
        state = NEW;
    }

    /**
     * 在线程中执行的方法
     */
    @Override
    public void run() {
        // 正在执行状态的时候调用 run 方法，直接返回
        if (state != NEW)
            return;

        if (null == callable)
            throw new NullPointerException();

        if (state == NEW) {
            sync.runSafety();
            try {
                // 更改执行状态为 COMPLETING
                state = COMPLETING;

                try {
                    // 执行方法并获得结果
                    result = callable.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    state = EXCEPTION;
                    // 异常时返回结果置为 null
                    result = null;
                }
            } finally {
                // 执行完毕之后将状态置为 COMPLETED
                state = COMPLETED;
                synchronized (this) {
                    this.notifyAll();
                }
            }
        }
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
        return state >= COMPLETED;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get() throws InterruptedException, ExecutionException {
        // 如果调用 get 的时候已经执行完毕，则直接返回结果
        if (isDone())
            return (V) result;
        // 如果没有计算完毕，则等待计算结果
        synchronized (this) {
            this.wait();
        }
        return (V) result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long deadline = System.currentTimeMillis() + timeout;
        long interval;
        for (; ; ) {
            if (Thread.currentThread().isInterrupted()) {
                // 如果当前线程已经中断
                throw new InterruptedException();
            }
            if (isDone()) {
                return (V) result;
            }
            //
            if (sync.tryAcquireNanos(1, unit.toNanos(10))) {
                try {
                    if (timeout > 0) {
                        interval = deadline - System.currentTimeMillis();
                        if (interval <= 0 && !isDone()) {
                            // 等待超时了
                            throw new TimeoutException();
                        }
                    }
                } finally {
                    sync.release(0);
                }
            }
        }
    }
}
