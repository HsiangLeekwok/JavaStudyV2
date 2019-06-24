# MySQL架构与存储引擎

## MySQL 逻辑架构

实例 -> database -> schema??(保留字) -> table // database == schema

```sql
-- 创建demo数据库
create database demo;
-- 显示数据库列表
show databases;
-- 删除 schema demo，也即删除数据库 demo
drop schema demo;
```

```sql
-- 显示当前实例的数据目录
show variable like '%datadir%';
-- 数据目录下面一个文件夹表示一个数据库，其下很多数据表文件 *.frm/*.ibd
-- .frm --> 表结构文件

```

1、连接器
    
    native C APP, JDBC, ODBC, .net, php, python, perl, ruby, vb
    当 MySQL 启动(MySQL服务器就是一个进程)，等待客户端连接；
    每一个客户但链接请求，服务器都会新建一个线程处理(如果是线程池的话，则是分配一个空闲的线程)；
    每个线程独立，拥有各自的内存处理空间；
    如果这个请求只是查询，没有什么问题，但当两个线程修改同一块内存时会引发数据同步问题。

2、连接池

    Authentication - Thread Reuse - Connection Limits - Check Memory - Caches

    SQL Interface、Parser、Optimizer、Caches & Buffer

3、查询优化，解析流程

![鱼骨图](weixin_pic_20190624120101.png)

4、存储引擎

    MyISAM, InnoDB, Cluster, Falcon, Archive, Federated, Merge, Memory
    Partner, Community, Custom
    
查询 mysql 支持的所有存储引擎

```sql
-- 查看所有支持的存储引擎
show engines;

-- 查询默认支持的存储引擎
SHOW VARIABLES LIKE '%storage_engine%';
```

|variable|value|
|:---|:---|
|default_storage_engine|InnoDB|
|default_tmp_storage_engine|InnoDB|
|disabled_storage_engines|InnoDB|
|internal_tmp_disk_storage_engine|InnoDB|

## 存储引擎 - MyISAM(5.5之前默认的存储引擎，之后变为 innodb)

    组成(堆表，数据和索引分开存放)：M有ISAM存储引擎由 .myd(d -> data) 和 .myi(i -> index) 组成
        innoDB是索引组织表，索引和数据放在一起
        
    MyISAM 支持表压缩：myisampack -b -f /usr/local/mysql/data/mall/testmysam.MYI
        压缩后不能插入数据，会变成 readonly
        
    特性：并发性与锁级别 - 表级锁
    支持全文检索
    支持数据压缩 ——> myisampack -r -f testmyisam.MYI
    
    适用场景：
        非事务型应用（数据仓库，报表，日志数据）
        只读应用
        空间类应用(空间函数，坐标)
    
```sql
-- 创建一个由 myisam 做存储引擎的表
CREATE TABLE testmyisam(
	id INT PRIMARY KEY
) ENGINE = myisam;

INSERT INTO testmyisam VALUES(1),(2),(3),(4);
```

```text
[root@localhost mall]# ll
总用量 1056
-rw-r-----. 1 mysql mysql   8556 6月  24 10:04 testmyisam.frm
-rw-r-----. 1 mysql mysql      0 6月  24 10:04 testmyisam.MYD
-rw-r-----. 1 mysql mysql   1024 6月  24 10:04 testmyisam.MYI
```
    
## 存储引擎 - Innodb(MySQL 5.5以及以后版本默认存储引擎)

    innodb_file_per_table:
        ON: 独立的表空间 ——> tablename.ibd
        OFF: 系统表空间 ——> ibdataX(5.6以前默认系统表空间)
        
    - 系统表空间无法简单的收缩文件大小
    - 独立表空间可以通过 optimize table 收缩系统文件
    - 系统表空间会产生 IO 瓶颈
    - 独立表空间可以同时向多个文件刷新数据
    
    建议：innodb使用独立表空间
    
    特性：
        innodb 是一种事务性存储引擎
        完全支持事务的 ACID 特性
        redo log 和 undo log
        innodb 支持行级锁(并发程度更高)
        
    适用场景：innodb 适用于大多数 OLTP(联机事务处理) 应用
    
|对比项|MyISAM|InnoDB|
|---|---|---|
|主外键|不支持|支持|
|事务|不支持|支持|
|行表锁|表锁，操作一条记录也会锁住整表，不适合高并发操作|行锁，操作时只锁某一行，不对其他行有影响，适合高并发的操作|
|缓存|之缓存索引，不缓存真实的数据|不仅缓存索引还缓存数据，对内存要求较高，而且内存大小对性能有决定性影响|
|表空间|小|大|
|关注点|性能|事务|
|默认安装|Y|Y|

## 存储引擎 - CSV

    组成：数据以文本方式存储在文件
        .csv 文件存储的内容
        .csm 文件存储表的元数据，如表状态和数据量
        .frm 表结构
        
    特性：
        以 csv 格式进行数据存储
        所有列都不能为 null
        不支持索引(不适合大表，不适合在线处理)
        可以对数据文件直接编辑(保存文本文件内容)，vi 直接编辑 .csv 文件，然后 flush tables就可以看到修改后的数据
        
```sql
CREATE TABLE mycsv(
	id INT NOT NULL, 
	c1 VARCHAR(10) NOT NULL,
	c2 CHAR(10) NOT NULL
)ENGINE csv;

INSERT INTO mycsv VALUES(1,'aaa','bbb'),(2,'ccc','ddd');

SELECT * FROM mycsv;

CREATE INDEX idx_id on mycsv(id);
-- [SQL]CREATE INDEX idx_id on mycsv(id);
-- [Err] 1069 - Too many keys specified; max 0 keys allowed
```

## 存储引擎 - Archive

    组成：以 zlib 对表数据进行压缩，磁盘 IO 更少，数据存储在 .arz 文件中
    特点：
        只支持 insert 和 select 操作
        只允许在 自增id列 上加索引
        
    适用场景：日志和数据采集应用
        
```sql
CREATE TABLE myarchive(
	id INT auto_increment NOT NULL,
	c1 VARCHAR(10),
	c2 CHAR(10),
	KEY (id)
) ENGINE = archive;

INSERT INTO myarchive(c1,c2) VALUE('aa','bb'),('cc','dd');

DELETE FROM myarchive WHERE id = 1;
-- [SQL]DELETE FROM myarchive WHERE id=1;
-- [Err] 1031 - Table storage engine for 'myarchive' doesn't have this option

UPDATE myarchive SET c1='aaaa' WHERE id = 1;
-- [SQL]UPDATE myarchive SET c1='aaaa' WHERE id = 1;
-- [Err] 1031 - Table storage engine for 'myarchive' doesn't have this option

CREATE INDEX idx_c1 ON myarchive(c1);
-- [SQL]CREATE INDEX idx_c1 ON myarchive(c1);
-- [Err] 1069 - Too many keys specified; max 1 keys allowed
```

## 存储引擎 - Memory

    - 文件系统存储特点，也称 heap 存储引擎，所以数据保存在内存中
    - 支持 hash 索引和 BTree 索引(hash 做等值查询很快，范围查询必须要用 BTree)
    - 所有字段都是固定长度，varchar(10) = char(10)
    - 不支持 Blob 和 Text 等大字段
    - Memory 存储引擎使用表级锁
    - 最大大小由 max_heap_table_size 参数决定
    
    容易混淆的概念：
        Memroy存储引擎表
        临时表：
            系统使用临时表(max_heap_table_size限制)
                1、超过限制使用 MyISAM 临时表
                2、未超限制使用 memory 表
            create temporary table 建立临时表 // 默认使用的是 memory
            
    使用场景：
        hash索引用于查找或者是映射表(如邮编和地区的对应表)
        用于保存数据分析中产生的中间表
        用户缓存周期性聚合数据的结果表
        
    memory数据易丢失，所以要求数据可再生
    
```sql
create table mymemory(
	id int, 
	c1 varchar(10), 
	c2 char(10), 
	c3 text
)engine = memory;
-- [Err] 1163 - The used table type doesn't support BLOB/TEXT columns

create table mymemory(
	id int, 
	c1 varchar(10), 
	c2 char(10)
)engine = memory;

create index idx_c1 on mymemory(c1);

show index from mymemory;
```

## 存储引擎 - Ferderated

    特点：
        - 提供了访问远程 mysql 服务器上表的方法
        - 本地不存储数据，数据全部放到远程服务器上
        - 本地需要保存表结构和远程服务器的连接信息
        
    适用场景：偶尔的统计分析及手工查询
    
    如何使用：默认禁止，启用需要在启动时增加 federated 参数
    mysql://user_name[:password]@hostname[:port_num]/db_name/table_name
    