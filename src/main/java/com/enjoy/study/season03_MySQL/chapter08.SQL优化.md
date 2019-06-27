# SQL 优化

## 策略1、尽量全职匹配

```sql
-- 符合索引是否充分用到了
explain select * from staffs where name ='July';
explain select * from staffs where name ='July' and age = 40;
explain select * from staffs where name ='July' and age = 40 and pos ='dev';
```

## 策略2、最佳左前缀原则

```sql
-- 没用到索引的例子
-- 符合索引 name,age,pos
explain select * from staffs where age=25 and pos ='dev';
explain select * from staffs where pos ='dev';

-- 使用到了索引
explain select * from staffs where name ='July';
```

带头大哥不能死，中间兄弟不能断

## 策略3、不在索引列上做任何操作

    不在索引列上做任何操作(计算/函数/(自动或手动的)类型转换),会导致索引失效而转向全表扫描
    
```sql
-- 在索引列上做任何操作导致索引失效而转向全表扫描
explain select * from staffs where left(name,4) = 'July';
```