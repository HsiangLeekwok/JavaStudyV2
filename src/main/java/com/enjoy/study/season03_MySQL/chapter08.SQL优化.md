# SQL 优化

## 优化策略

### 策略1、尽量全职匹配

```sql
-- 符合索引是否充分用到了
explain select * from staffs where name ='July';
explain select * from staffs where name ='July' and age = 40;
explain select * from staffs where name ='July' and age = 40 and pos ='dev';
```

### 策略2、最佳左前缀原则

```sql
-- 没用到索引的例子
-- 符合索引 name,age,pos
explain select * from staffs where age=25 and pos ='dev';
explain select * from staffs where pos ='dev';

-- 使用到了索引
explain select * from staffs where name ='July';
```

带头大哥不能死，中间兄弟不能断

### 策略3、不在索引列上做任何操作

    不在索引列上做任何操作(计算/函数/(自动或手动的)类型转换),会导致索引失效而转向全表扫描
    
```sql
-- 在索引列上做任何操作导致索引失效而转向全表扫描
explain select * from staffs where left(name,4) = 'July';
```

### 策略4、范围条件放最后

    这里的条件不是指的 where 语句里的范围条件，而是索引里的范围字段

```sql
explain select * from staffs where name = 'July' and pos = 'nanager' and age > 22;
```

### 策略5、覆盖索引尽量用

    尽量使用覆盖索引（只访问索引的查询（索引列和查询列一致）），减少 select *
    
### 策略6、不等于要慎用

    mysql 在使用不等于( != 或者 <> )的时候无法使用索引，会导致权标扫描
    
```sql
explain select * from staffs where name !='July'
```

### 策略7、null/not null有影响

    注意 null/not null 对索引的可能影响
    
```sql
mysql> explain select * from staffs where name is null;
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+------------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | filtered | Extra            |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+------------------+
|  1 | SIMPLE      | NULL  | NULL       | NULL | NULL          | NULL | NULL    | NULL | NULL |     NULL | Impossible WHERE |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+----------+------------------+
1 row in set, 1 warning (0.00 sec)

-- 权标扫描
mysql> explain select * from staffs where name is not null;
+----+-------------+--------+------------+------+-----------------------+------+---------+------+------+----------+-------------+
| id | select_type | table  | partitions | type | possible_keys         | key  | key_len | ref  | rows | filtered | Extra       |
+----+-------------+--------+------------+------+-----------------------+------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | staffs | NULL       | ALL  | idx_staffs_nameAgePos | NULL | NULL    | NULL |    1 |   100.00 | Using where |
+----+-------------+--------+------------+------+-----------------------+------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.00 sec)
```

### 策略8、like 查询要当心

    like 以通配符开头('%abc...') mysql 索引失效会变成权标扫描
    
```sql
-- 全表扫描
explain select * from staffs where name like '%aa%'
explain select * from staffs where name like '%aa'

-- 使用索引的模糊查询
explain select * from staffs where name like 'aa%'
-- 覆盖索引解决模糊查询
explain select name,age,pos from staffs where name like '%aa%'
```

### 策略9、字符类型加引号

    字符串不加引号索引失效
    
```sql
-- 索引列上有强制类型转换，导致索引失效
explain select * from staffs where name = 917
```

### 策略10、or 改 union 效率更高

```sql
-- 全表扫描
mysql> explain select * from staffs where name = 'July' or name = 'z3';
+----+-------------+--------+------------+------+-----------------------+------+---------+------+------+----------+-------------+
| id | select_type | table  | partitions | type | possible_keys         | key  | key_len | ref  | rows | filtered | Extra       |
+----+-------------+--------+------------+------+-----------------------+------+---------+------+------+----------+-------------+
|  1 | SIMPLE      | staffs | NULL       | ALL  | idx_staffs_nameAgePos | NULL | NULL    | NULL |    1 |   100.00 | Using where |
+----+-------------+--------+------------+------+-----------------------+------+---------+------+------+----------+-------------+
1 row in set, 1 warning (0.01 sec)

mysql> explain select * from staffs where name = 'July'
    -> union
    -> select * from staffs where name = 'z3';
+------+--------------+------------+------------+------+-----------------------+-----------------------+---------+-------+------+----------+-----------------+
| id   | select_type  | table      | partitions | type | possible_keys         | key                   | key_len | ref   | rows | filtered | Extra           |
+------+--------------+------------+------------+------+-----------------------+-----------------------+---------+-------+------+----------+-----------------+
|  1   | PRIMARY      | staffs     | NULL       | ref  | idx_staffs_nameAgePos | idx_staffs_nameAgePos | 74      | const |    1 |   100.00 | NULL            |
|  2   | UNION        | staffs     | NULL       | ref  | idx_staffs_nameAgePos | idx_staffs_nameAgePos | 74      | const |    1 |   100.00 | NULL            |
| NULL | UNION RESULT | <union1,2> | NULL       | ALL  | NULL                  | NULL                  | NULL    | NULL  | NULL |     NULL | Using temporary |
+------+--------------+------------+------------+------+-----------------------+-----------------------+---------+-------+------+----------+-----------------+
3 rows in set, 1 warning (0.00 sec)
```

### 葵花宝典

    全值匹配我最爱，最左前缀要遵守
    带头大哥不能死，中间兄弟不能断
    索引列上少计算，范围之后全失效
    like 百分写最右，覆盖索引不写 *
    不等空值还有 or，索引影响要注意
    varchar 引号不可丢，sql 优化有诀窍。
    
## 批量导入

### 方法1、insert 语句优化

- 提前关闭自动提交
- 尽量使用批量 insert 语句
- 可以使用 MyISAM 存储引擎

### 方法2、load data infile

使用 load data infile 比一般的 insert 语句快 20 倍

```sql
-- 导出 sql 语句到文件
select * into OUTFILE 'D:\\produce.txt' from product_info

-- 从文件中导入 sql 语句并执行
load data infile 'D:\\product.txt' into table product_info
```