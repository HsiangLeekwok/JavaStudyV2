# MySQL 事务

## 为什么需要事务

    很多软件都是多用户，多程序，多线程的，对同一个表可能同时又很多人在用，为保持数据的一致性，所以提出了事务的概念
    A 给 B 划账，A 的账户 -1000 元，B 的账号就要 +1000 元，这两个 update 语句必须作为一个整体来执行，不然 A 扣钱了 B 没有加钱，这种状况很难处理
    
## 什么存储引擎支持事务

    1、查看数据库下面是否支持事务(innodb 支持)
        show engines;
    2、查看mysql当前默认的存储引擎
        show variables like '%storage_engine%';
    3、查看某张表的存储引擎
        show create table 表明;
    4、对于表的存储结构的修改
        建立innodb表: create table ... type = innodb; alert table ... type = innodb;
        
## 事务的特性

    事务应该具有 4 个属性：原子性、一致性、隔离性、持久性，这四个属性通常称为 ACID 特性。
    - 原子性(atomicity)：一个事物是一个不可分割的工作单位，事务中包括的所有操作要么都做，要么都不做
    - 一致性(consistency)：事务必须是使数据库从一个一致性状态变到另一个一致性状态，一致性与原子性是密切相关的。
    - 隔离性(isolation)：一个事物的执行不能被其他事务干扰，即一个事物内部的操作及使用的数据对并发的其他事务是隔离的，并发执行的各个事务之间不能互相干扰
    - 持久性(durability)：也称永久性(permanence)，指一个事物一旦提交，它对数据库中数据的改变就因该是永久性的，接下来的其他操作或故障不应该对其有任何影响
    
## 原子性(atomicity)

    一个事务必须被视为一个不可分割的最小单元，整个事务中的所有操作要么全部提交成功，要么全部失败。
    对于一个事务来说，不可能只执行其中的一部分操作
    
## 一致性(consistency)

    一致性是指事物将数据库从一种一致性转换到另外一种一致性状态，在事务开始之前和事务结束之后数据库中的数据完整性没有被破坏

## 持久性(durability) 或 永久性(permanence)

    一旦事务提交，则其所做的修改就会永久保存到数据库中，此时即使系统崩溃，已提交的修改数据也不会丢失
    
    并不是数据库的角度能完全解决的，还涉及到备份等操作才可以
    
## 隔离性(isolation)

    隔离性级别
        - 未提交读
        - 已提交读
        - 可重复读
        - 可串行化
        
### 事务的特性

事务并发问题

- 脏读，事务A都去了事务B更新的数据，然后事务B回滚操作，那么A读取到的数据是脏数据
- 不可重复读(行数并没有增加)，事务A多次读取同一数据，事务B在事务A多次读取的过程中，对数据做了更新并提交，导致事务A多次读取同一数据时，结果不一致
- 幻读(行数变化了)，系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDEF等级，但系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这叫做幻读


    不可重复读和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除
    解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表
        
### 未提交读

```sql
-- 实验脏读，需要设置一下隔离级别为 UNCOMMITED
-- session 1
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

BEGIN
select * from account;
update account set balance=balance-50 where id=1
```

```sql
-- session 2
SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

BEGIN
select * from account;// 会读到 session 1 中未提交的更改
```

### 已提交读

```sql
-- 实验已提交读，需要设置一下隔离级别为 COMMITED
-- session 1
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

BEGIN
update account set balance=balance-50 where id=1
select * from account;
```

```sql
-- session 2
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

BEGIN
select * from account;// 此时读到的是session 1 未更改前的值，读取不到 session 1 未提交的更改，解决了脏读
-- 如果 session 1提交了，此时再次读，会读到 session 1更改之后的值，这叫不可重复读(多次读取的值不一样)
```

### 可重复读

```sql
-- 实验可重复读，不需要设置隔离级别(默认级别即可，需要重新开启查询 session)
-- session 1
BEGIN
update account set balance=balance-50 where id=1
select * from account;// 未提交，此时session 2 读取的是修改前的值
COMMIT// 提交之后，session 2读取的还是修改前的值
```

```sql
-- session 2
BEGIN
select * from account;// session 1 无论提交与否，这里读取的都是session1修改前的值
commit;// 只有session2提交或回滚之后再读，才是session1提交或回滚之后的正确值
```

### 可串行化

```sql
-- 可串行化解决了幻读问题，但是是通过锁表来解决
-- session1
SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;-- 设置串行化
BEGIN
select * from account;
COMMIT;-- 未提交之前，session 2的更改会一直阻塞
```

```sql
-- session2
SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN
select * from account;
-- 锁表，解决幻读
insert into account values(5,'deer',500);// session1 未提交或回滚之前，这里的更改都会阻塞
-- 只有session1提交或回滚之后才会执行这里的 insert 语句
```
    
事务的隔离级别

|隔离级别|脏读|不可重复读|幻读|
|:---|:---:|:---:|:---:|
|读未提交(read-uncommited)|是|是|是|
|不可重复读(read-commited)|否|是|是|
|可重复读(repeatable-read)|否|否|是(mysql 否)|
|串行化(serializable)|否|否|否|

### 事务的语法

    - 开启事务
        begin
        start transaction(推荐语法)
        begin work
        
    - 事务回滚 rollback
    - 事务提交 commit
    - 还原点 savepoint
    
```sql
-- 还原点 demo
set autocommit = 0; -- 需要设置不自动提交
begin
	select * from t_lock_2;
	
	insert into t_lock_2 values(11,9);
	savepoint s1;
	
	insert into t_lock_2 values(12,10);
	savepoint s2;
	
	insert into t_lock_2 values(13,11);
	savepoint s3;
	
rollback to savepoint s2; -- 回滚到 s2，此时 s3 已经丢失
```