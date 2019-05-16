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
    volatile 关键字，最轻量的同步机制，之恩那个保证共享变量之间的可见性，但不能保证其原子性
        常用在一个线程写，多个线程读的场景
    
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

    wait()
    notify/notifyAll

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

    notify和notifyAll应该用谁？尽量用notifyAll来通知所有等待的线程

    等待超时模式实现一个连接池
    
    调用yield()、sleep()、wait()、notify()等方法对锁有何影响？
        yield(): 让出时间片，但不释放锁，当前线程会转到就绪状态
        sleep(): 睡眠，但不释放锁，当前线程处于阻塞状态
    yield 会让出CPU的执行权，不会释放锁（就绪）
    sleep 不会释放锁（阻塞）
    wait 会释放锁，被唤醒时去竞争锁（阻塞）
    notify/notifyAll 不释放锁，一般放在同步代码块的最后一行（阻塞）

### 第二课：线程的并发工具

#### Fork/Join体现了分而治之

    什么是分而治之？
        大问题 -> 分割成相同的小问题，小问题之间无关联
        设计思想
        策略
        
    十大计算机经典算法：快速排序、堆排序、归并排序、二分查找、线性查找、深度优先、广度优先、Dijkstra、动态规划、朴素贝叶斯分类
        快速排序、归并排序、二分查找就是使用了Fork/Join
        归并排序：对于给嘀咕的一组数据，利用递归与分治技术将数据序列化分成为越来越小的半子表，在对半子表排序后，在用递归方法将排序好的半子表合并成为越来越大的有序序列
            为了提升性能，有时我们在半子表的个数小于某个书（比如15）
            
    Fork/Join 原理
        fork/join框架：就是在必要的请跨国下，将一个大人物，进行拆分（fork)成若干个小人物(拆到不可再拆时)，再将一个个的小人物运算的结果进行join汇总
        fork/join里有工作密取方式：先完成工作的线程会从别的线程中取出还未完成的任务，做完之后再还回去
        
    Fork/Join实战
        Fork/Join的同步用法同时演示返回结果值：统计整形数组中所有元素的和
        Fork/Join的异步用法同时演示不要求返回值：遍历指定目录(含子目录)，寻找指定类型的文件
        
#### CountDownLatch的作用、应用场景和实战

    await的线程可以是多个
    countDown可以在同一个线程中扣减多次
    
    await的扣减只能扣减一次
    由外部线程来协调线程的同步
    
#### CyclicBarrier的作用、应用场景和实战

    初始化的数量必须和等待数量相同
    
    await方法可以多次使用，配合action可以看到多次执行action
    由线程本身自己来协调同步
    
    CountDownLatch 和 CyclicBarrier 的区别：
    1、CyclicBarrier 的计数器可以反复使用，在某个屏障上反复协调使用。
    2、在协调线程之间同时运行的区别：CountDownLatch 是由外部线程来做，CyclicBarrier 由线程本身自己来协调
    3、CountDownLatch 的计数器跟线程数没有关系，CyclicBarrier 则跟线程数密切相关的
    4、CountDownLatch 不能汇总线程之间的计算结果，但 CyclicBarrier 可以在传入action之后进行线程之间结果的汇总。
    
#### Semaphore的作用、应用场景和实战

    对公共资源有限的场景做流量控制，和被争夺的资源本身没有任何关系
    Semaphore注意事项：
        如果没有事先调用acquire就直接调用release的话，会一直增加数量
        
#### Exchange的作用、应用场景和实战

    两个线程之间进行数据交换，JDK保证交换的过程是线程安全的
    
#### Callable、Future、FutureTask

    FutureTask 既可以当作 Runnable 投给线程执行，也可以拿到结果
    可以中断
    
### 第三课：原子操作 CAS

#### CAS(Compare And Swap)
    
    什么是原子操作？如何实现原子操作？
    属于乐观锁
    JDK CAS 机制 ---> 无锁化编程
    
CAS的原理

- 利用了现代处理器都支持的CAS指令
- 循环这个指令，直到成功为止

CAS的问题

- ABA问题
- 开销问题
- 只能保证一个共享变量的原子操作

#### 原子操作类的使用

#### JDK中相关原子操作类的使用

    更新基本类型类：AtomicBoolean、AtomicInteger、AtomicLong
    更新数组类：AtomicIntegerArray、AtomicLongArray、AtomicReferenceArray
    更新引用类型：AtomicReference、AtomicMarkableReference、AtomicStampedReference
    原子更新字段类：……………………
    
### 第四课：显式锁和AQS

#### 显式锁
    
    申明一个锁，手动去释放、加锁的，叫显式锁
    Lock接口和和新方法
        lock()
        unlock()
        tryLock()
        
    Lock接口和synchronized的比较
        synchronized的消耗比显式锁少
        
    可重入锁ReentrantLock、公平锁和非公平锁
    
    Lock接口的使用范式
    
    可重入锁
    
    ReadWriteLock接口和读写锁ReentrantReadWriteLock，什么情况下用读写锁？
    
    Condition接口
    
    用Lock和Condition实现等待通知
    
    了解LockSupport工具
    作用：
        阻塞一个线程
        唤醒一个线程
        构建同步组件的基础工具
    park开头的方法：阻塞一个线程
    unpart开头的方法：唤醒一个线程
    
#### AQS(AbstractQueuedSynchronizer)

    CLH 队列锁
    什么是AQS？学习它的必要性
        AQS 使用方式和其中的设计模式
        了解其中的方法
        实现一个类似于 ReentrantLock 的锁
        
    AQS 中的数据结构 - 节点和同步队列
    
    都展示同步状态获取与释放
    
    其他同步状态获取与释放
        共享是同步状态获取与释放
        都展示超时同步状态 获取
        
    再次实战：实现一个奇葩的三元共享同步工具类
    
#### Condition 分析

    一个Condition包含一个等待队列
    
    区分同步队列与等待队列
    
    既然是3个线程共享，为什么在加锁或者释放锁的时候getState所在的for循环里没有做同步？
    
#### 回头看Lock的实现
    
    了解 ReentrantLock 的实现
        锁的可重入
        公平和非公平
        
    将类似于ReentrantLock的锁实现修正为可重入
    了解ReentrantReadWriteLock的实现
    
### 第五课：并发容器

#### 预备知识 - Hash

    Hsah，一般翻译做“散列”，也有直接音译为“哈希”的，就是把任意长度的输入（又叫做预映射）通过散列算法变换成固定长度的输出，该输出就是散列值。这种转换是一种压缩映射，也就是，散列值的空间通常元小雨输入的控件，不同的输入可能会
    简单的说就是一种将任意长度的消息压缩到某一固定长度的消息摘要的函数。
    
    常用的hash函数：直接取余法、乘法取整法、平方取中法
    MD5、SHA-1：加密算法（摘要算法），不可逆
    
    开放寻址：当出现hash冲突的时候，在当前地址继续往后找
    再散列：再次散列
    链地址法：冲突的元素在当前地址后面用链表组合起来
    
#### 预备知识 - 位运算

    我们日常使用的是十进制，计算机中庸二进制
    十进制：逢十进一
    二进制：逢二进一
    
    Java位运算中一些常用运算：
        位与 & ：1&1=1 、0&0=0、1&0=0
        位或 | ：1|1=1,0|0=0， 1|0=1
        位非 ~ ：~1=0， ~0=1
        位异或 ^: 1^1=0, 1^0=1, 9^9=0
        有符号右移：>> 若正数，高位补0，负数高位补1
        有符号左移 <<
        无符号右移：>>>无论正负，高位均补0
        
    有趣的取模性质：取模 a%2(2^n)等价于 a&(n-1)，所以在map理的数组个数一定是2的乘方数，计算key值在哪个元素中的时候，就用到了位运算来快速定位。
    
    位运算的运用场景
        哪里可以用到位运算：
            Java中的类修饰符、成员变量修饰符、方法修饰符
            Java容器中的HashMap和ConcurrentHashMap的实现
            权限控制或商品属性
            简单的可逆加密(1^1=0; 0^1=1)
        实战：将位运算用在权限控制、商品属性上
            节省很多代码
            效率高
            属性变动影响小
            不直观
    
     如何快速的判定一个数是奇数还是偶数？
 
 #### 1.7种的HashMap的死循环，会形成一个环形的数据结构
 
 #### ConcurrentHashMap基本用法
 
 ##### JDK1.7种ConcurrentHashMap的实现
    
    初始化，初始化之后segments不再变化，变化的只是其下table的容量和每个节点下的链表长度
    get
    put
    rehash
    remove
    size    尽量不要用size来统计是否有数据，会造成重量级别的锁住整个map来统计，用isEmpty
    isEmpty
    containsValue   尽量不要使用，同size一样，会锁住整个map
    
    弱一致，所以get和containsKey可能会得到不一致的内容
 
 ##### JDK1.8中ConcurrentHashMap的实现
 
    取消了1.7中的segment，直接把table提到segment的高度，锁的是table，用红黑树取代了table下的链表结构
    核心数据结构和属性
        sizeCtrl
    核心方法
        tabAt
        casTabAt
        setTabAt
    构造方法
    初始化
    get
    put
    remove
    transfer
    treeifyBeen
    size
    
    红黑树和链表的转换：table长度大于8的时候会转换成红黑树，红黑树小于6时转换成链表
 
 #### 并发下的Map常见面试题汇总
 
 - HashMap和HashTable有什么区别？
 
 - Java中的另一个线程安全的与HashMap极其类似的类是什么？同样是线程安全，它与HashTable在线程同步上有什么不同？
 
    HashTable锁住的是整个table，ConcurrentHash采用的是分段锁(1.7)和CAS(1.8)结合
    
 - HashMap & ConcurrentHashMap的区别？
 
    HashMap线程不安全，ConcurrentHashMap线程安全
    HashMap允许键值对为null，ConcurrentHashMap不允许
 
 - 为什么ConcurrentHashMap比HashTable效率高？
 
    锁住了整个表，容易造成阻塞，ConcurrentHashMap采用分段锁，锁的粒度小
    
- ConcurrentHashMap锁机制具体分析(JDK1.7 VS JDK1.8)?

- ConcurrentHashMap在JDK1.8种，为什么要使用内置锁 synchronized 来代替重入锁ReentrantLock？

    1.8中synchronized已经做了很大的优化，ReentrantLock需要消耗资源

- 1.8下ConcurrentHashMap简单介绍

    数据结构，常用的put、get方法是怎么实现的
    sizeCtl来控制初始化和扩容的大小，以及标记当前是否正在扩容或正在初始化
    Node节点 -> 
        TreeNode
        TreeBin
    get：如果是table节点，直接返回，否则按照链表或红黑树查找
    put：table为空，直接放在table节点，否则table加锁按照链表或红黑树方式插入，链表数大于8则转换成红黑树，最后检测是否需要扩容
        扩容：工作线程会协助进行扩容，为了避免扩容冲突，每个线程会按照一定的步长进行选择table节点进行辅助扩容

- ConcurrentHashMap的并发度是什么？

    默认16
 
 #### Concurrent其他系列容器
 
    ConcurrentSkipListMap 和 ConcurrentSkipListSet，需要了解什么是SkipList?
    跳表(概率数据结构)
        Redis、Lucence都使用了跳表来进行快速查找
    TreeMap 和 TreeSet：TreeSet是对TreeMap的包装
    
    ConcurrentLinkedQueue，LinkedList的并发版本，无界非阻塞队列
        add：在队尾加入一个元素
        offer
        peek
        poll：拿到并移除头元素
    
    写时复制容器：写的时候直接复制一个新容器，并把旧容器里的内容重指向新容器
        - CopyOnWriteArrayList
        - CopyOnWriteArraySet
        使用于绝大多数读，极少写的场景：
            白名单、黑名单等
        缺点：数据一致性(只能保证最终一致性)、空间换时间（内存开销大，2份）
        避免：
            创建时一次性固定大小；
            批量提交修改
        
#### 阻塞队列 BlockingQueue
        
    什么是阻塞队列：阻塞的元素变动方法，生产者消费者模式下用得多
        阻塞的插入：队列满，插入会阻塞
        阻塞的移除：队列空，移除会阻塞
    常用方法
    常用阻塞队列：
        ArrayBlokinQueue：一个由数组结构组成的有界阻塞队列
        LinkedBlockingQueue：一个由链表结构组成的有界阻塞队列
        PriorityBlockingQueue：一个支持优先级排序的无界阻塞队列
        DelayQueue：一个使用优先级队列实现的无界阻塞队列，平时用得最多，提供延时获取元素方式（时间到期后才能拿得到数据）
        SynchronousQueue：异步不存储元素的阻塞队列
        LinkedTransferQueue：一个由链表结构组成的无界阻塞队列
        LinkedBlockingDeque：一个由链表结构组成的**双向**阻塞队列
            头尾都可以拿和取
            Fork/Join里的工作密取用到了这个队列
            
    阻塞队列的实现原理：作业
    
### 第六课：线程池

#### 什么事线程池？为什么要用线程池？

    好处：
        1、降低资源的消耗 Thread
        2、提高相应速度 T1：创建时间、T2：运行时间、T3：销毁时间，线程池大部分时间只需要T2即可
        3、提高线程的可管理性

#### 造轮子，先实现一个我们自己的线程池

    1、缺省的线程数量

#### 再来看看JDK中的线程池和工作原理

    JDK中的线程池和工作机制
        线程池创建时各个参数的含义，面试必问
            corePerSize：核心线程数量
                提交任务时，线程数量小于此值，则创建新线程
            maxSize: 最大线程数量
                如果queue队列已满，则继续创建线程，直到数量达到max
            queue：任务队列
                如果线程数量超过corePerSize，则新提交进来的任务放进队列里等待被处理
            factory：线程创建工厂
            policy：满任务时处理策略
                如果线程数量超过max，则执行满任务处理策略（共4种），默认抛出异常（其余三种：直接丢弃，提交任务的线程来执行，
        提交任务
            exuecute：提交任务，不需要得到任务的返回结果
            submit：提交任务，返回一个Future<V>，可以用其拿取计算结果
        关闭线程池
            shutdown：只终止空闲的线程
            shutdownNow：不管线程是否空闲，都会调用interrupte方法
        
    Executor框架
        Executor
            ExecutorService
                AbstractExecutorService
                    ThreadPoolExecutor
                        ScheduledThreadPoolExecutor
                        
    合理配置线程池
        通过任务特性来合理配置
            CPU密集型：线程数不要超过CPU数量+1（Runtime.getRuntime().getAvaliableProeces()）
            IO密集型：磁盘、网络
            混合型：CPU + IO 都有，尽量拆分成两个线程池，分开CPU密集和IO密集（如果两者相差悬殊，则没必要拆分）

#### 如何使用好

#### JDK与兴义的线程池详解

    FixedThreadPool：固定线程池，其中任务队列太大Integer.MaxValue
    SingleThreadExecutor
    CachedThreadPool
    WorkStealingPool
    ScheculedThreadPoolExecutor：重点
        需要catch业务代码，pool本身不会抛出异常且会吞掉业务代码中的异常
        
    CompletionService：
        先完成的任务先获得结果

### 第七课：并发安全

什么是线程安全性？怎么才能做到线程安全？

- 栈封闭        推荐方式    AD-HOC：不要让线程自己的内容泄漏到外面去。方法内的局部变量
- 无状态的类
- 让类不可变：给成员加final关键词、只提供成员的查询方法；final如果修饰的是对象，如果对象对外可以访问，则不属于安全发布
- volatile：只保证类的可见性，不能保证安全性。用在一个线程写，多个线程读的场景
- 加锁和CAS：最常用的保证线程安全的手段，并不是所有情况下加锁都能保证线程安全————死锁
- 安全的发布：getter返回的成员变量
    // 可以在线程安全模式下返回list
    private List<Integer> list= Collections.synchronizedList(new ArrayList<>(3));
    // 如果成员是对象
- ThreadLocal   推荐方式

Servlet：Servlet不是线程安全的，跨请求的时候是不安全的

成员变量，多线程下的写，必定有线程安全问题

如：统计访问次数

```Java
private int count;
count ++;
```

Spring的IOC初始化的时候是加了锁的，且用的是普通HashMap，初始化完毕之后IOC就不需要加锁了，因为整个生命周期只会加载1次，其余时间都是读，所以没必要用ConcurrentHashMap


线程不安全引发的问题

死锁：指两个或两个以上的进程在执行过程中，由于竞争资源或者由于批次通信而造成的一种阻塞线程，若无外力作用，他们都将无法推进下去。此时称系统处于死锁状态或系统产生了死锁。

    必要条件：
        1、多个操作者(M >= 2)争夺多个资源( N >= 2 ), M >= N，即资源数小于等于操作者
        2、争夺资源的顺序不对
        
    解决：
        1、通过某种顺序，按照顺序拿锁
        2、增加第三把锁，然后再按照先后顺序锁定
        
活锁：

线程饥饿：优先级造成的某些线程一直获取不到锁的情况

性能和思考：

    影响性能的因素：上下文切换、内存同步……
    减少锁的竞争：缩小锁的范围，减少锁的粒度，锁分段，替换独占锁，避免多余的锁
    
    先保证程序正确，确实达不到性能要求，再去优化————黄金原则
    一定已测试为基准
    
线程安全的单例模式(面试高频考点之一)

    用双重检查锁定来创建单例，它真的是安全的吗？
    
    new 一个对象，至少有3步：
        1、内存中分配空间
        2、空间初始化
        3、把这个控件的地址给我们的引用
    
    解决之道：
        懒汉式
            延迟初始化占位类模式
        饿汉式
            直接 static 属性并且初始化：private static xxx=new xxx();
            
            
## 第八章 实战

### 1、架构师是什么？了解架构师的方方面面

#### 架构设计、软件开发

    确认需求
    系统分解
    技术选型
    制定技术规格说明
    
#### 开发管理

    深深介入开发的方方面面
        作用：设计架构、救火、布道
        效果：公关、信念
        职责：产品架构、基础服务架构
    
#### 沟通协调

    域用户、产品、上级、团队成员

### 2、架构师技能 - 构建基础服务架构要从需求中抽取共性

### 3、分析需求，研究共性