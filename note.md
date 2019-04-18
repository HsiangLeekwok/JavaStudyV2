# 第二期笔记

## JAVA 并发编程

### 第一课：线程基础、线程之间的共享和协作

#### 什么是进程和线程

    进程：操作系统进行资源分配的最小单位
    线程：CPU调度的最小单位，不能够独立于进程存在，必须依赖进程

#### CPU核心数和线程数的关系

#### CPU时间片轮转机制

    RR调度，时间片轮转调度
    
#### 澄清并行和并发

    并行：parallel，并行表示可以同时进行处理的数量
    并发：concurrent，与时间段有关，并发实际上是交替执行
    
#### 高并发变成的意义、好处和注意事项

    充分利用资源，加快反应速度
    可以使代码模块化、异步化、简单化
    
    注意事项：
    1、线程安全
    2、
    3、OS限制：
        Linux 一个进程最多能开1k个线程，能打开1024个句柄
        WIndows最多可开2k个线程
        
#### 认识Java里的线程

    Java 程序天生就是多线程的
        通过类Thread
        通过接口 Runnable
        
    Thread 和 Runnable 的区别：手写代码Thread 和 Runnable 的区别
        Java 对线程的抽象：Thread
        Runnable：对任务和业务逻辑的抽象
        
#### 有开始就有结束，怎么样才能让Java里的线程安全停止工作：

    stop()
    interrupt()
    isInterrupted()
    interrupted() static 方法
    
    JDK里的线程是协作式的，而不是抢占式
    
    手写代码，区别interrupt()、isInterrupted()和静态类的interrupted()方法。区别中断 Thread 和 Runnable 的方式
    手写代码，尝试捕获 InterruptException 后 interrupt 标志位的状态
    
    注意：处于死锁状态的线程是不会处理 InterruptException 的
    
#### 线程常用方法和线程的状态

    深入理解 run() 和 start()
    sleep()
    了解 yield()：将线程从运行转到可运行状态
    join()：可以保证两个线程顺序的执行
    线程优先级：默认值为5，优先级高低，完全由OS决定，不一定会起到作用
    守护线程：setDeamon(true)，会随着主线程一起消亡，其中的 finally 方法不一定会执行
![avatar](./images/20190416213244.png)

#### 什么是线程间的共享

    synchronized 内置锁
        用处和用法
            同步代码块
            方法
        对象锁，synchronized用在方法、代码块的时候，锁住的是当前对象
        类锁：synchronized用在静态方法时，锁住的是当前类的class对象。类锁和对象锁可以完全并行运行，互不干扰。
    volatile 关键字，最轻量的同步机制
    
    错误的加锁和原因分析
    
#### ThreadLocal 辨析

    ThreadLocal 的使用
    
    实现解析
    
    引发内存泄漏分析

        强引用：Object object = new Object(); // 所有的 new 对象都是强引用，栈里有引用指向堆内的对象，则一直不会释放
        软引用：SoftReference // GC时如果内存足够，则不会回收，但如果内存不够则回收
        弱引用：WeakRefernece // 只要发生了垃圾回收，弱引用所指向堆内的实例都会被回收
        虚引用：
        
    ThreadLocal 线程不安全
    
#### 线程间的协作

等待和通知的标准范式
等待：
sync(对象){
    while(条件不满足){
        对象.wait()
    }
    // 业务逻辑
}
通知：
syn(对象){
    // 业务逻辑，改变条件
    对象.notify/notifyAll();
}

### 第二课：线程的并发工具