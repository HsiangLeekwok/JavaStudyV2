# 慢查询

## 慢查询配置

慢查询定义以及作用

    慢查询日志，顾名思义就是查询慢的日志，是指 mysql 记录所有执行超过 long_query_time 参数设定的时间阈值的 sql 语句的日志
    该日志能为 sql 语句的优化带来很好的帮助
    默认情况下，慢查询日志是关闭的，要使用慢查询日志功能，首先要开启这个功能。

### 慢查询基本配置

```sql
-- 系统中默认慢查询时间设定为10s
show variables like '%long_query_time%';
-- 设置慢查询时间为5s
set global long_query_time = 5;
-- 开启慢查询记录
set global slow_query_log = 1;
```

```text
# Time: 2019-06-24T14:13:02.263927Z
# User@Host: root[root] @  [192.168.32.1]  Id:    13
# Query_time: 0.000278  Lock_time: 0.000076 Rows_sent: 2  Rows_examined: 2
SET timestamp=1561385582;
SELECT * FROM `mall`.`testdemo` LIMIT 0,1000;
```

### 慢查询解读

## 慢查询分析

### mysqldumpslow

语法

```text
mysqldumpslow -s r -t 10 slow-mysql.log

-s order(c,t,l,r,at,al,ar)
    c: 总次数
    t: 总时间
    l: 锁的时间
    r: 总数据行
    at, al, ar: t,l,r的平均数，如at=总时间/总次数
-t top 指定获取前面几条作为结果输出
```

```text
shell> mysqldumpslow -s t -t 10 /usr/local/mysql/data/mysql-slow.log

[root@localhost data]# mysqldumpslow -s t -t 10 localhost-slow.log

Reading mysql slow query log from localhost-slow.log
Count: 6  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=1.0 (6), root[root]@[192.168.32.1]
  SHOW TABLE STATUS LIKE 'S'

Count: 15  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=1.1 (17), root[root]@[192.168.32.1]
  #

Count: 2  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=2.0 (4), root[root]@[192.168.32.1]
  SHOW COLUMNS FROM `mall`.`json_user`

Count: 2  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=2.0 (4), root[root]@[192.168.32.1]
  SHOW COLUMNS FROM `mall`.`testdemo`

Count: 2  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=2.0 (4), root[root]@[192.168.32.1]
  SELECT * FROM `mall`.`json_user` LIMIT N,N

Count: 2  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=0.0 (0), root[root]@[192.168.32.1]
  SHOW CREATE TABLE `mall`.`testdemo`

Count: 1  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=4.0 (4), root[root]@[192.168.32.1]
  SHOW TABLE STATUS

Count: 2  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=2.0 (4), root[root]@[192.168.32.1]
  SELECT * FROM `mall`.`testdemo` LIMIT N,N

Count: 1  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=4.0 (4), root[root]@[192.168.32.1]
  SHOW FULL TABLES WHERE Table_type != 'S'

Count: 2  Time=0.00s (0s)  Lock=0.00s (0s)  Rows=0.0 (0), root[root]@[192.168.32.1]
  SHOW CREATE TABLE `mall`.`json_user`

```

### pt_query_digest

用于分析 mysql 慢查询的一个工具，与 mysqldumpslow 相比，其分析结果更具体，更完善。

```text
[root@localhost bin]# ./pt-query-digest --explain h=127.0.0.1,u=root,p=root1234% /usr/local/mysql/data/localhost-slow.log

# 310ms user time, 130ms system time, 25.38M rss, 296.63M vsz
# Current date: Mon Jun 24 22:42:21 2019
# Hostname: localhost.localdomain
# Files: /usr/local/mysql/data/localhost-slow.log
# Overall: 43 total, 14 unique, 3.07 QPS, 0.00x concurrency ______________
# Time range: 2019-06-24T14:12:48 to 2019-06-24T14:13:02
# Attribute          total     min     max     avg     95%  stddev  median
# ============     ======= ======= ======= ======= ======= ======= =======
# Exec time           14ms    56us     1ms   316us   657us   217us   254us
# Lock time            4ms       0   536us    82us   247us   118us       0
# Rows sent             47       0       7    1.09    3.89    1.67       0
# Rows examine          47       0       7    1.09    3.89    1.67       0
# Query size         1.40k      17      45   33.35   42.48    6.50   31.70

# Profile
# Rank Query ID                         Response time Calls R/Call V/M   I
# ==== ================================ ============= ===== ====== ===== =
#    1 0x8085D806F3631D0D30FE5C20326...  0.0026 18.9%     6 0.0004  0.00 SHOW TABLE STATUS
#    2 0x19B98FCFFA2FD73B13E16349DF1...  0.0017 12.7%     2 0.0009  0.00 SHOW COLUMNS
#    3 0x898255B1BE4F8C3044AE35A1828...  0.0017 12.6%    15 0.0001  0.00 ADMIN INIT DB
#    4 0x2A7BD220D27C992001EFD386649...  0.0013  9.4%     2 0.0006  0.00 SHOW COLUMNS
#    5 0x25A365DB79A532446F84D5F9620...  0.0011  8.2%     2 0.0006  0.00 SHOW COLUMNS
#    6 0x8235C430635BC0B9DE28D78C2B1...  0.0009  6.7%     2 0.0005  0.00 SELECT mall.json_user
#    7 0x344246A22B252F10F441010CA22...  0.0008  5.8%     2 0.0004  0.00 SELECT mall.t_lock_?
#    8 0x5FD7FB80CAE67BE3137340E50F1...  0.0007  5.1%     2 0.0003  0.00 SELECT mall.testdemo
#    9 0xF6C6D60B8B46C010445D20E44CF...  0.0006  4.5%     1 0.0006  0.00 SHOW TABLE STATUS
#   10 0xFD550B0478FD477DAEAD6D99AC2...  0.0006  4.5%     2 0.0003  0.00 SHOW CREATE TABLE
#   11 0xAE71829D033595CB926AA425CF7...  0.0005  3.7%     2 0.0003  0.00 SHOW CREATE TABLE
#   12 0x72833D3A18ACDED00CF6DE6C18C...  0.0005  3.5%     1 0.0005  0.00 SHOW TABLES
# MISC 0xMISC                            0.0006  4.6%     4 0.0002   0.0 <2 ITEMS>

# Query 1: 0.46 QPS, 0.00x concurrency, ID 0x8085D806F3631D0D30FE5C2032672827 at byte 9459
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:49 to 2019-06-24T14:13:02
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count         13       6
# Exec time     18     3ms   318us   541us   429us   515us    70us   457us
# Lock time     19   696us    70us   215us   116us   214us    51us   126us
# Rows sent     12       6       1       1       1       1       0       1
# Rows examine  12       6       1       1       1       1       0       1
# Query size    13     200      33      34   33.33   33.28    0.50   31.70
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
SHOW TABLE STATUS LIKE 'testdemo'\G

# Query 2: 0.17 QPS, 0.00x concurrency, ID 0x19B98FCFFA2FD73B13E16349DF10CE0E at byte 2409
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:49 to 2019-06-24T14:13:01
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time     12     2ms   716us     1ms   865us     1ms   210us   865us
# Lock time     26   930us   394us   536us   465us   536us   100us   465us
# Rows sent      8       4       2       2       2       2       0       2
# Rows examine   8       4       2       2       2       2       0       2
# Query size     5      72      36      36      36      36       0      36
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms  ################################################################
#  10ms
# 100ms
#    1s
#  10s+
# Tables
#    SHOW TABLE STATUS FROM `mall` LIKE 'json_user'\G
#    SHOW CREATE TABLE `mall`.`json_user`\G
SHOW COLUMNS FROM `mall`.`json_user`\G

# Query 3: 1.07 QPS, 0.00x concurrency, ID 0x898255B1BE4F8C3044AE35A182869033 at byte 4455
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:48 to 2019-06-24T14:13:02
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count         34      15
# Exec time     12     2ms    56us   192us   114us   159us    36us    98us
# Lock time      0       0       0       0       0       0       0       0
# Rows sent      0       0       0       0       0       0       0       0
# Rows examine   0       0       0       0       0       0       0       0
# Query size    31     450      30      30      30      30       0      30
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us  ##########################################
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
administrator command: Init DB\G

# Query 4: 2 QPS, 0.00x concurrency, ID 0x2A7BD220D27C992001EFD386649C0CB5 at byte 3775
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:50 to 2019-06-24T14:12:51
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      9     1ms   613us   663us   638us   663us    35us   638us
# Lock time     16   597us   233us   364us   298us   364us    92us   298us
# Rows sent      6       3       1       2    1.50       2    0.71    1.50
# Rows examine   6       3       1       2    1.50       2    0.71    1.50
# Query size     4      70      35      35      35      35       0      35
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
# Tables
#    SHOW TABLE STATUS FROM `mall` LIKE 't_lock_1'\G
#    SHOW CREATE TABLE `mall`.`t_lock_1`\G
SHOW COLUMNS FROM `mall`.`t_lock_1`\G

# Query 5: 0.20 QPS, 0.00x concurrency, ID 0x25A365DB79A532446F84D5F9620A20A2 at byte 9231
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:52 to 2019-06-24T14:13:02
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      8     1ms   526us   594us   560us   594us    48us   560us
# Lock time     12   433us   182us   251us   216us   251us    48us   216us
# Rows sent      8       4       2       2       2       2       0       2
# Rows examine   8       4       2       2       2       2       0       2
# Query size     4      70      35      35      35      35       0      35
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
# Tables
#    SHOW TABLE STATUS FROM `mall` LIKE 'testdemo'\G
#    SHOW CREATE TABLE `mall`.`testdemo`\G
SHOW COLUMNS FROM `mall`.`testdemo`\G

# Query 6: 0.17 QPS, 0.00x concurrency, ID 0x8235C430635BC0B9DE28D78C2B1FD52E at byte 1739
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:49 to 2019-06-24T14:13:01
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      6   908us   249us   659us   454us   659us   289us   454us
# Lock time      7   264us    66us   198us   132us   198us    93us   132us
# Rows sent      8       4       2       2       2       2       0       2
# Rows examine   8       4       2       2       2       2       0       2
# Query size     6      90      45      45      45      45       0      45
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
# Tables
#    SHOW TABLE STATUS FROM `mall` LIKE 'json_user'\G
#    SHOW CREATE TABLE `mall`.`json_user`\G
# EXPLAIN /*!50100 PARTITIONS*/
SELECT * FROM `mall`.`json_user` LIMIT 0,1000\G
# *************************** 1. row ***************************
#            id: 1
#   select_type: SIMPLE
#         table: json_user
#    partitions: NULL
#          type: ALL
# possible_keys: NULL
#           key: NULL
#       key_len: NULL
#           ref: NULL
#          rows: 2
#      filtered: 100.00
#         Extra: NULL

# Query 7: 2 QPS, 0.00x concurrency, ID 0x344246A22B252F10F441010CA22D1A10 at byte 4678
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:50 to 2019-06-24T14:12:51
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      5   784us   351us   433us   392us   433us    57us   392us
# Lock time      6   239us   118us   121us   119us   121us     2us   119us
# Rows sent     29      14       7       7       7       7       0       7
# Rows examine  29      14       7       7       7       7       0       7
# Query size     6      88      44      44      44      44       0      44
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
# Tables
#    SHOW TABLE STATUS FROM `mall` LIKE 't_lock_2'\G
#    SHOW CREATE TABLE `mall`.`t_lock_2`\G
# EXPLAIN /*!50100 PARTITIONS*/
SELECT * FROM `mall`.`t_lock_2` LIMIT 0,1000\G
# *************************** 1. row ***************************
#            id: 1
#   select_type: SIMPLE
#         table: t_lock_2
#    partitions: NULL
#          type: index
# possible_keys: NULL
#           key: b
#       key_len: 5
#           ref: NULL
#          rows: 9
#      filtered: 100.00
#         Extra: Using index

# Query 8: 0.20 QPS, 0.00x concurrency, ID 0x5FD7FB80CAE67BE3137340E50F11D0B1 at byte 6041
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:52 to 2019-06-24T14:13:02
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      5   693us   278us   415us   346us   415us    96us   346us
# Lock time      5   208us    76us   132us   104us   132us    39us   104us
# Rows sent      8       4       2       2       2       2       0       2
# Rows examine   8       4       2       2       2       2       0       2
# Query size     6      88      44      44      44      44       0      44
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
# Tables
#    SHOW TABLE STATUS FROM `mall` LIKE 'testdemo'\G
#    SHOW CREATE TABLE `mall`.`testdemo`\G
# EXPLAIN /*!50100 PARTITIONS*/
SELECT * FROM `mall`.`testdemo` LIMIT 0,1000\G
# *************************** 1. row ***************************
#            id: 1
#   select_type: SIMPLE
#         table: testdemo
#    partitions: NULL
#          type: ALL
# possible_keys: NULL
#           key: NULL
#       key_len: NULL
#           ref: NULL
#          rows: 2
#      filtered: 100.00
#         Extra: NULL

# Query 9: 0 QPS, 0x concurrency, ID 0xF6C6D60B8B46C010445D20E44CF56324 at byte 1306
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: all events occurred at 2019-06-24T14:12:48
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          2       1
# Exec time      4   612us   612us   612us   612us   612us       0   612us
# Lock time      2    95us    95us    95us    95us    95us       0    95us
# Rows sent      8       4       4       4       4       4       0       4
# Rows examine   8       4       4       4       4       4       0       4
# Query size     1      17      17      17      17      17       0      17
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
SHOW TABLE STATUS\G

# Query 10: 0.20 QPS, 0.00x concurrency, ID 0xFD550B0478FD477DAEAD6D99AC25C241 at byte 6954
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:52 to 2019-06-24T14:13:02
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      4   606us   153us   453us   303us   453us   212us   303us
# Lock time      0       0       0       0       0       0       0       0
# Rows sent      0       0       0       0       0       0       0       0
# Rows examine   0       0       0       0       0       0       0       0
# Query size     4      70      35      35      35      35       0      35
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
SHOW CREATE TABLE `mall`.`testdemo`\G

# Query 11: 2 QPS, 0.00x concurrency, ID 0xAE71829D033595CB926AA425CF73CE72 at byte 4228
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: 2019-06-24T14:12:50 to 2019-06-24T14:12:51
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          4       2
# Exec time      3   502us   239us   263us   251us   263us    16us   251us
# Lock time      0       0       0       0       0       0       0       0
# Rows sent      0       0       0       0       0       0       0       0
# Rows examine   0       0       0       0       0       0       0       0
# Query size     4      70      35      35      35      35       0      35
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
SHOW CREATE TABLE `mall`.`t_lock_1`\G

# Query 12: 0 QPS, 0x concurrency, ID 0x72833D3A18ACDED00CF6DE6C18C897F2 at byte 847
# This item is included in the report because it matches --limit.
# Scores: V/M = 0.00
# Time range: all events occurred at 2019-06-24T14:12:48
# Attribute    pct   total     min     max     avg     95%  stddev  median
# ============ === ======= ======= ======= ======= ======= ======= =======
# Count          2       1
# Exec time      3   473us   473us   473us   473us   473us       0   473us
# Lock time      2    89us    89us    89us    89us    89us       0    89us
# Rows sent      8       4       4       4       4       4       0       4
# Rows examine   8       4       4       4       4       4       0       4
# Query size     2      43      43      43      43      43       0      43
# String:
# Databases    mall
# Hosts        192.168.32.1
# Users        root
# Query_time distribution
#   1us
#  10us
# 100us  ################################################################
#   1ms
#  10ms
# 100ms
#    1s
#  10s+
SHOW FULL TABLES WHERE Table_type != 'VIEW'\G
```