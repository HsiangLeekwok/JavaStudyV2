# MySQL 锁

## 为什么需要锁

    到淘宝上买一件上牌，上牌只有一件库存，这时如果还有另外的人要买，那么如何解决究竟是谁买到的呢？
        1、先从库存表中取出物品数量
        2、插入订单
        3、付款后插入付款表信息
        4、更新商品数量
    在这个过程中，使用锁可以对有限的资源进行保护，解决隔离和并发的矛盾
    
## 锁的概念

    - 锁是计算机协调多个进程或线程并发访问某一资源的机制
    - 在数据库中，数据也是一种拱许多用户共享的资源，如何保证数据并发访问的一致性、有效性是素有数据库必须解决的一个问题，锁冲突也是影响数据库并发访问性能的一个重要因素
    - 锁对数据库而已显得尤其重要，也更加复杂
    
## MySQL 中的锁

    MySQL的锁机制比较简单，其最显著的特点是不同的存储引擎支持不同的锁机制
    比如：
        M有ISAM 和 Memory 存储引擎采用的是表级锁(table-level locking)
        InnoDB 存储引擎既支持行级锁(row-level locking)，也支持表级锁，但默认情况下是采用行级锁
        
    - 表级锁：开销小，枷锁快，不会出现死锁，锁的粒度大，发生锁冲突的概率最高，并发度最低
    - 行级锁：开销大，加锁慢，会出现死锁，锁的粒度小，发生锁冲突的概率最低，并发度也最高
    - 页面锁(gap锁，间隙锁)：开销和加锁时间介于表锁和行锁之间，会出现死锁，锁的粒度介于表锁和行锁之间，并发度一般
    
    仅从锁的角度来说，表级锁更适合以查询为主，只有少量按索引条件更新数据的应用，比如 OLAP 系统
    行锁更适合于有大量按索引条件并发更新少量不同数据，同时又有并发查询的应用，如一些在线事务(OLTP)处理系统

## MyISAM锁

MySQL的表级锁有两种模式

- 表共享都锁 table read lock
- 表独占写锁 table write lock

|锁模式|none|读锁|写锁|
|:---:|:---:|:---:|:---:|
|读锁|是|是|否|
|写锁|是|否|否|

### 共享读锁

    共享读锁的语法：
        lock table 表名 read
        unlock tables // 解锁
    
    - 同一个session里面，如果加了共享读锁，则只能读，不能插入、更新数据，且别的表也不能插入、更新数据，会直接报错
    - 另外一个session里面，也只能读不能插入，插入时会等待，但别的表可以插入
    
```sql
-- session 1
create table testmysam(
	id int primary key
) engine = myisam;

insert into testmysam values(1),(2),(3);

lock table testmysam read;
-- [SQL]lock table testmysam read;
-- 受影响的行: 0
-- 时间: 0.013s

insert into testmysam values(4);
-- [SQL]insert into testmysam values(4);
-- [Err] 1099 - Table 'testmysam' was locked with a READ lock and can't be updated

insert into account values(4,'aa',123);
-- [SQL]insert into account values(4,'aa',123); 别的表在这个 session 中也不能更新
-- [Err] 1100 - Table 'account' was not locked with LOCK TABLES
```

```sql
-- session 2
insert into testmysam values(4);
-- [SQL]insert into testmysam values(4); 等待中...
```

### 共享写锁

    共享读锁的语法：
        lock table 表名 write
        unlock tables // 解锁
        
    总结：
        读：
        - 对 MyISAM 表的读操作，不会阻塞其他用户对同一表的读请求
        - 对 MyISAM 表的读操作，不会阻塞当前 session 对标的读，对表进行修改会报错
        - 一个 session 使用 lock table 命令给表 t 加了读锁，这个 session 可以查询锁定表中的记录，单更新或访问其他表都会提示错误
        写：
        - 对 MyISAM 表的写操作，会阻塞其他用户对同一表的读和写操作
        - 对 MyISAM 表的写操作，当前 session 可以对本表做 crud，但对其他表进行操作会报错
        
```sql
-- session 1
LOCK TABLE testmysam WRITE;

INSERT testmysam value(33);

DELETE FROM testmysam where id=33;

select * from testmysam;

select * from account;
-- [SQL]select * from account;// 不能查询其他表
-- [Err] 1100 - Table 'account' was not locked with LOCK TABLES
```

```sql
-- session 2
select * from testmysam;
-- [SQL]select * from testmysam; 等待中...
```
        
## InnoDB锁

### InnoDB 行锁

    在 mysql 中的 innodb 引擎支持行锁，锁的是索引
    
行锁

- 共享锁又称读锁，当一个事务对某几行上读锁时，允许其他事务对这几行进行读操作，但不允许进行写操作，也不允许其他事务给这几行上排它锁，但允许上读锁
- 排它锁又称写锁，当一个事务对某几行上写锁时，不允许其他事务写，但允许读，更不允许其他事务给这几行上任何锁，包括写锁

语法：

    共享锁，lock in share mode
        select * from 表 where 条件 lock in share mode;
    排它锁 for update
        select * from 表 where 条件 for update;
        
    注意：
        1、两个事务不能锁同一个索引
        2、insert、delete、update在事务中都会自动默认加上排它锁
        3、行锁必须有索引才能实现，否则会自动锁权标，那么就不是行锁了
        
```sql
create table testdemo(
id int(255) not null,
c1 varchar(300) character set utf8 collate utf8_general_ci null default null,
c2 int(50) null default null,
primary key(id),
index idx_c2(c2) using btree
) engine = innodb;

insert into testdemo values(1,'1',1),(2,'2',2);
```

```sql
-- session 1
-- 开启事务
BEGIN

-- 上写锁
select * from testdemo where id=1 for update;
```

```sql
-- session 2
update testdemo set c1='2' where id=2;
-- 由于 session 1 是行锁，因此这里更新是成功的
```

排它锁

```sql
-- session 1
BEGIN
update testdemo set c1='1' where id = 1; -- 自动增加排它锁

-- session 2
update testdemo set c1='1' where id=1; -- 会一直等待，直到 session 1 提交或 rollback
```

无索引列默认是表锁

```sql
-- session 1
BEGIN
update testdemo set c1='1' where c1 = '1'; // c1 字段没有索引，此时锁整表

-- session 2
update testdemo set c1='2' where c1='2'; // 等待直至释放锁或等待超时
```

间隙锁(gap)

间隙锁锁定主键 demo：

```sql
-- session 1
create table t_lock_1(a int primary key);

insert into t_lock_1 values(10),(11),(13),(20),(40);
select * from t_lock_1;

begin
    -- 所有小于等于 13 的行都被锁定，不能插入不能更新
	select * from t_lock_1 where a <= 13 for update;
	insert into t_lock_1 values(0);-- 同一个 session 里面可以随意插入修改，因为锁只针对不同 session 来说的
commit;-- commit或rollback之前，session2会一直阻塞在 insert 19 这里
rollback;
```

```sql
-- session 2
begin

    insert into t_lock_1 values(21); -- 插入成功，21 不在 session1 的锁定范围内
    insert into t_lock_1 values(19); -- 插入等待中，19 在 session1 的锁定范围之外，但由于间隙锁的存在，13之后的20也会被锁住，从而导致无法插入19
    -- 只有等到 session1 commit或者rollback之后插入才会从阻塞状态返回并且插入数据
commit;
```

间隙锁锁定非主键的索引列 demo

```sql
-- session 1
create table t_lock_2 (a int primary key, b int, key(b));
insert into t_lock_2 values(1,1),(3,1),(5,3),(8,6),(10,8);
select * from t_lock_2;
-- 1 3 5 8 10
-- 1 1 3 6 8

begin

	select * from t_lock_2 where b = 3 for update; -- 锁定非pk索引
	
commit;
```

```sql
-- session2
-- 1 3 5 8 10		pk
-- 1 1 3 6 8
-- 锁住的范围(1,3],(3,6)
begin

	select * from t_lock_2 where a = 5 lock in share mode;-- 与session1的锁定行相同，会阻塞
	insert into t_lock_2 values(4,2);-- 间隙锁锁住了二级索引 b=3 这一行，其左右的1、6也会同时被锁住
	insert into t_lock_2 values(6,5);-- 二级索引
	insert into t_lock_2 values(4,0);-- 插入成功，二级索引不锁主键，且插入值在锁定范围之外
	insert into t_lock_2 values(6,7);-- 插入成功，二级索引不锁主键，且插入值在锁定范围之外
	
	select * from t_lock_2;
commit;
```

### 死锁

MySQL 5.6 中查询死锁

```sql
select 
	r.trx_id waiting_trx_id,
	r.trx_mysql_thread_id waiting_thread,
	r.trx_query waiting_query,
	b.trx_id blocking_trx_id,
	b.trx_mysql_thread_id blocking_thread
FROM
	information_schema.INNODB_LOCK_WAITS w
INNER JOIN
	information_schema.INNODB_TRX b on b.trx_id = w.blocking_trx_id
inner JOIN
	information_schema.INNODB_TRX r on r.trx_id = w.requesting_trx_id;
```

MySQL 5.7 中查询死锁

```sql
select 
	w.waiting_pid waiting_pid, 
	w.blocking_pid blocking_pid, 
	w.waiting_query waiting_query,
	w.sql_kill_blocking_query kill_query
from sys.innodb_lock_waits w
```