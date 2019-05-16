package com.enjoy.study.ch006;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/10 16:37<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/> 实现一个自己的阻塞队列
 * <b>Description</b>: 实现一个自己的阻塞队列，只实现 get 和 take 方法即可
 */
public class HomeWork20190509_BlockingQueue<E> implements BlockingQueue<E> {

    /**
     * 可重入锁
     */
    private ReentrantLock lock = new ReentrantLock();
    /**
     * 放入元素时的等待队列
     */
    private Condition putCondition = lock.newCondition();
    /**
     * 拿取元素时的等待队列
     */
    private Condition takeCondition = lock.newCondition();
    /**
     * 默认最大队列长度
     */
    private static final int DEFAULT_MAX = 2000;
    /**
     * 队列中元素存放链表
     */
    private List<E> queue = new ArrayList<>();
    /**
     * 当前阻塞队列中元素的个数
     */
    private int size = 0;
    /**
     * 队列容器的最大个数
     */
    private int capacity;

    public HomeWork20190509_BlockingQueue() {
        // 这里指定固定大小，@Test不允许多个构造方法，所以这里只做测试用
        capacity = 10;
    }

    /**
     * 指定阻塞队列容器的大小，超过这个大小时再添加元素会阻塞，这里去掉了带参数的方法，@AppTest 只允许无参构造方法
     */
//    public HomeWork20190509_BlockingQueue(int capacity) {
//        if (capacity <= 0 || capacity >= DEFAULT_MAX) {
//            this.capacity = DEFAULT_MAX;
//        } else {
//            this.capacity = capacity;
//        }
//    }
    @Override
    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                if (size < capacity) {
                    // 新元素放入队尾
                    queue.add(e);
                    size += 1;
                    takeCondition.signal();
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " wanna put an object, but queue is full.");
                putCondition.await();
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public E take() throws InterruptedException {
        Object object;
        lock.lock();
        try {
            while (true) {
                if (size > 0) {
                    // 从队头拿去元素
                    object = queue.get(0);
                    queue.remove(0);
                    size -= 1;
                    putCondition.signal();
                    break;
                }
                System.out.println(Thread.currentThread().getName() + " wanna take an object, but queue is empty.");
                takeCondition.await();
            }
        } finally {
            lock.unlock();
        }
        return (E) object;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return 0;
    }

    private class PutWorker extends Thread {

        private CountDownLatch latch;
        private HomeWork20190509_BlockingQueue<String> _queue;
        private int times = 0;

        public PutWorker(CountDownLatch latch, HomeWork20190509_BlockingQueue<String> _queue) {
            this.latch = latch;
            this._queue = _queue;
        }

        @Override
        public void run() {
            try {
                latch.await();
                while (!isInterrupted()) {
                    _queue.put(getName() + " have put " + times++ + " times");
                    //sleep(1);
                }
            } catch (InterruptedException ignore) {
                //e.printStackTrace();
            }
            System.out.println(getName() + " interrupted.");
        }
    }

    private class TakeWorker extends Thread {

        private CountDownLatch latch;
        private HomeWork20190509_BlockingQueue<String> _queue;

        public TakeWorker(CountDownLatch latch, HomeWork20190509_BlockingQueue<String> _queue) {
            this.latch = latch;
            this._queue = _queue;
        }

        @Override
        public void run() {
            try {
                latch.await();
                while (!isInterrupted()) {
                    String taken = _queue.take();
                    System.out.println(getName() + " taken: " + taken);
                    //sleep(1);
                }
            } catch (InterruptedException ignore) {
                //e.printStackTrace();
            }
            System.out.println(getName() + " interrupted.");
        }
    }


    @Test
    public void test() throws InterruptedException {
        int threadSize = 10;
        CountDownLatch latch = new CountDownLatch(threadSize);
        HomeWork20190509_BlockingQueue<String> queue = new HomeWork20190509_BlockingQueue<>();
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            // put 线程
            Thread thread = new PutWorker(latch, queue);
            thread.setName("PutWorker_" + i);
            threads.add(thread);
            thread.start();
            latch.countDown();
        }
        for (int i = 0; i < 5; i++) {
            Thread thread = new TakeWorker(latch, queue);
            thread.setName("TakeWorker_" + i);
            threads.add(thread);
            thread.start();
            latch.countDown();
        }

        Thread.sleep(500);
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
}
