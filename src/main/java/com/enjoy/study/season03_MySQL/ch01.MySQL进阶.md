# MySQL 进阶

## MySQL 安装

### 单实例安装以及跟随官方安装步骤所有坑的解决办法

    1、准备工作，下载 mysql 二进制文件：http://mirrors.163.com
    2、上传 mysql 的 tar 包到 linux 中的 /soft 文件夹中
    3、具体详细步骤在 linux 中操作：
        groupadd mysql # 创建用户组
        useradd -r -g mysql mysql # 在用户组下面添加 mysql 用户
        cp /soft、mysql-5.7.26-linux-glibc2.12-x86_64.tar.gz /usr/local # 复制安装包到 /usr/local 目录
        cd /usr/local # 切换目录
        tar -zxvf mysql-5.7.26-linux-glibc2.12-x86_64.tar.gz # 解压
        ln -s /usr/local/mysql-5.7.26-linux-glibc2.12-x86_64 mysql # 创建一个名为 mysql 的快捷方式
        cd mysql # 切换到 mysql 目录下
        mkdir mysql-files # 创建新目录
        chmod 770 mysql-files # 更改目录权限
        chown -R mysql . # 更当前目录所有者为 mysql 用户
        chgrp -R mysql . # 更改当前目录为 mysql 用户组
        bin/mysqld --initialize --user=mysql # 初始化 mysql，此时会生成一个密码，保存密码后面用(如果后续步骤不出错的话)
        bin/mysql_ssl_rsa_setup # 安装密钥
        chown -R root . # 改回当前目录的所有者为 root
        
        // 这一步是 mysql 官方安装步骤中没有的，否则会出错
        mkdir data # 创建一个新目录
        
        chown -R mysql data mysql-files 
        bin/mysqld_safe --user=mysql &  # 设置 mysql 运行的账户，此时会出错，如下
        
        mysqld_safe error: log-error set to '/var/log/mariadb/mariadb.log', however file don't exists. Create writable for user 'mysql'.
        
        解决方案：
        // 查看 mysql 安装配置默认查找顺序，或者有冲突时的加载顺序
        shell> /usr/local/mysql/bin/mysqld --verbose --help | grep -A 1 'Default options'
        Default options are read from the following files in the given order:
        /etc/my.cnf /etc/mysql/my.cnf /usr/local/mysql/etc/my.cnf ~/.my.cnf 
        
        // CentOS 7 安装完毕之后会默认生成一个配置文件到 /etc/my.cnf，查看一下里面的配置内容，其中的配置文件夹是没有权限访问的，所以会出错
        vi /etc/my.cnf
        [mysqld]
        datadir=/var/lib/mysql
        socket=/var/lib/mysql/mysql.sock
        # Disabling symbolic-links is recommended to prevent assorted security risks
        symbolic-links=0
        # Settings user and group are ignored when systemd is used.
        # If you need to run mysqld under a different user or group,
        # customize your systemd unit file for mariadb according to the
        # instructions in http://fedoraproject.org/wiki/Systemd
        
        [mysqld_safe] 
        log-error=/var/log/mariadb/mariadb.log
        pid-file=/var/run/mariadb/mariadb.pid
        
        #
        # include all files from the config directory
        #
        !includedir /etc/my.cnf.d
        
        // 删除 /etc/my.cnf 并且备份一下
        mv /etc/my.cnf /etc/my.cnfbak
        
        // 重新初始化 msyql 流程
        cd /usr/local/mysql # 回到 mysql 目录下
        bin/mysqld --initialize --user=mysql # 重新初始化 mysql，此时会重新生成密码，记录一下
        cd data # 进入 data 目录下 
        ll # 查看文件列表，此时已经有数据了，说明 mysql 初始化成功
        
        // 返回 mysql 文件夹
        cd /usr/local/mysql
        bin/mysql_ssl_rsa_setup # 重新安装密钥
        chown -R root . # 设置当前目录所有者为 root
        chown -R mysql data mysql-files # 设置 data 和 mysql-files 目录的所有者为 mysql
        bin/mysqld_safe --user=mysql & # 用用户 mysql 启动 mysqld daemon，此时 mysql 启动完毕
        
        // 查看 mysql 是否启动，查看当前进程中是否有 mysqld 进程即可
        ps -ef | grep mysqld
        
    4、一些可选项安装
        // 将安装之后生成的服务复制一份到 /etc/init.d目录下，以后可以直接开启mysql
        cp support-files/mysql.server /etc/init.d/mysql.server
    
    5、设置开机启动
        chkconfig --list # 查看开机启动列表，目前应该只有跟网络有关的两项开机启动项
            netconsole      0:关    1:关    2:关    3:关    4:关    5:关    6:关
            network         0:关    1:关    2:开    3:开    4:开    5:开    6:关
            
        chkconfig mysql.server on # 设置 mysql.server 为开机启动，此时再查询一遍列表，会看到 mysql.server 为 on 状态
    
    6、配置环境变量
        vi /etc/profile # 打开配置文件，在文件最后增加一行 
        
        ...
        export PATH=/usr/local/mysql/bin:$PATH
        
        保存退出
        
        // 使 profile 生效
        source /etc/profile
        
        
    7、设置防火墙，允许相关的 mysql 端口
        
        // 关闭 linux 的防火墙，否则用 Navicat 可能无法登录 mysql ——————不推荐
        systemctl stop firewalld.service
        
        // ————以下方式推荐
        // 允许相应的 MySQL 端口通过防火墙
        firewall-cmd --permanent --zone=public --add-port=3306-3309/tcp
        // 让防火墙重载规则
        firewall-cmd --reload
        // 查看已允许的防火墙端口列表
        firewall-cmd --list-port
        
    8、登录 msql 并在本机设置 root 可以通过网络方式登录
        // 此时在 shell 下直接输入 mysql 就可以登录了
        mysql -uroot -p'这里刚才生成的密码'
        // 重设 root 的密码，否则以后登录输入密码很费劲
        set passowrd = 'root1234%';
        // 设置 root 可以通过网络登录
        GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root1234%' WITH GRANT OPTION;
        // 刷新权限策略
        FLUSH PRIVILEGES;

### 多实例安装步骤

1、新建 /etc/my.cnf 配置如下
    
```text
[mysqld]
sql_mode="STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER"
    
[mysqld_multi]
mysqld=/usr/local/mysql/bin/mysqld_safe
mysqladmin=/usr/local/mysql/bin/mysqladmin
log=/var/log/mysqld_multi.log

[mysqld1]
server-id=11
socket=/tmp/mysql.sock1
port=3307
datadir=/data1
user=mysql
performance_schema=off
innodb_buffer_pool_size=32M
skip_name_resolve=1
log_error=error.log
pid-file=/data1/mysql.pid1

[mysqld2]
server-id=12
socket=/tmp/mysql.sock2
port=3308
datadir=/data2
user=mysql
performance_schema=off
innodb_buffer_pool_size=32M
skip_name_resolve=1
log_error=error.log
pid-file=/data2/mysql.pid2
```
        
2、部署

    mkdir /data1 # 新建两个目录
    mkdir /data2
        
    chown mysql.mysql /data{1..2} # 更改这两个目录的所有者为 mysql 组的 mysql 用户
        
    # 初始化
    mysqld --initialize --user=mysql --datadir=/data1 # 初始化第一个实例，记录密码 4uif0FD%cyd-
    mysqld --initialize --user=mysql --datadir=/data2 # 初始化第二个实例，记录密码 ow>rp%bqf4yI
        
    # 初始化成功之后将多实例启动文件复制到系统初始化目录，并设置开机启动
    cp /usr/local/mysql/support-files/mysqld_multi.server /etc/init.d/mysqld_multid
    chkconfig mysqld_multid on
        
    # 查看多实例的状态
    mysqld_multi report
    # 如果提示没有安装 perl，则需要安装 perl，可能需要很长时间，需要安装各种依赖
    yum -y install perl perl-devel
        
    # 安装完毕 perl 之后再查看状态，已经能看到两个实例，但实例都未运行
    
    # 启动多实例后再查看状态，连个实例都已经运行起来了
    mysqld_multi start
    
    # 分别登录两个实例并更改 root 的密码，且授权给 root( -p 的密码是初始化的时候记录的密码)
    mysql -uroot -S /tmp/mysql.sock1 -p'4uif0FD%cyd-' -P3307
    mysql -uroot -S /tmp/mysql.sock2 -p'ow>rp%bqf4yI' -P3308
    
    mysql> set password='root1234%';
    mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY 'root1234%';
    mysql> flush privileges;
    
    # 使用 Navicat 分别登录两个实例
    
3、多实例配置开机启动

第一种：

```text
cd /etc/init.d
vi auto_start_mysql_multi_instance

#!/bin/sh
# chkconfig: 2345 64 36
# desciption: Auto start all mysql instance
#

export PATH=/usr/local/mysql/bin:$PATH
/etc/init.d/mysqld_multid start

// 保存后

chkconfig auto_start_mysql_multi_instance on
reboot
..............
mysqld_multi report
```

第二种：

```text
vi /etc/init.d/mysqld_multid

# 在 basedir=xxx 这一行前面添加一行环境变量
export PATH=/usr/local/mysql/bin:$PATH
// 保存退出重启再查询状态
```

### MySQL 权限        

权限赋予
    
```sql
GRANT ALL PRIVILEGES -- 赋予所有权限
ON *.* -- 所有数据库和所有表 db.table
TO 'root'@'%' -- root 用户在所有的ip都可以访问
IDENTIFIED BY 'root1234%' -- root用户的登录密码
WITH GRANT OPTION; -- 这一部分可以不需要
```

```sql
GRANT SELECT -- 赋予 select 权限
	ON mall.* -- 在 mall 数据库中的所有表
	TO 'dev'@'192.168.22.%' -- 给用户 dev 且登录在 IP 段 192.168.22.*
	IDENTIFIED BY '123' -- 登陆密码 123
	WITH GRANT OPTION;
```

查看所赋予的权限

```sql
SHOW GRANTS FOR 'dev'@'192.168.22.%';
```


取消授权
```sql
REVOKE SELECT on mall.* from 'dev'@'192.168.244.%';
```

用户标识

    用户名 + IP
    
```sql
CREATE TABLE `account` (
	`id` INT (11) NOT NULL,
	`name` VARCHAR (50) DEFAULT NULL,
	`balance` INT (255) DEFAULT NULL,
	PRIMARY KEY (`id`),
	KEY `idx_balance` (`balance`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

INSERT INTO account VALUES(1,'lilei',900);
INSERT INTO account VALUES(2,'hanmei',100);
INSERT INTO account VALUES(3,'lucy',250);
INSERT INTO account VALUES(4,'tom',0);

-- 给用户 dev 赋予只能选择某几个字段的权限
GRANT SELECT(id, name) ON mall.account TO 'dev'@'192.168.22.%';

-- dev 登录之后就不能 select * from account 了
```

角色

    mysql 中不存在角色，只有用户
    
```sql
-- 查看是否允许用户代理，默认是 OFF
SHOW VARIABLES like '%proxy%';

-- 开启用户代理
SET GLOBAL check_proxy_users = 1;
-- 开启用户密码加密
SET GLOBAL mysql_native_password_proxy_users = 1;


-- 创建角色(实际就是创建一个用户)
CREATE USER 'dev_role';

-- 再创建两个用户
CREATE USER 'deer';
CREATE USER 'enjoy';

-- 把用户加入角色里面，这个 SQL 会执行出错，需要在后台给 root 赋予 proxy 权限(mysql 5.7)之后再执行这两句 
GRANT proxy ON 'dev_role' TO 'deer';
GRANT proxy ON 'dev_role' TO 'enjoy';

-- 需要在 mysql 后台给 root 赋予 proxy 权限，然后刷新权限
GRANT PROXY ON ''@'' TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- 给角色统一赋予相应的权限就可以了
GRANT SELECT(id,name) ON mall.account to 'dev_role';

-- 此时用户 deer/enjoy 登录后只能查询 id/name，不能  select *

```

## MySQL 进阶

### MySQL 数据类型 - int

|类型|字节|最小值(有符号)|最小值(无符号)|最大值(有符号)|最大值(无符号)|
|:----------:|:---:|:----------:|:----------:|:----------:|:----------:|
|TINYINT|1|-128|0|127|255|
|SMALLINT|2|-32768|0|32767|65535|
|MEDIUMINT|3|-8388608|0|8388607|16777215|
|INT|4|-2147483648|0|2147483647|4294967295|
|BIGINT|8|-9223372036854775808|0|9223372036854775807|18446744073709551615|

```sql
-- 无符号数据应用
CREATE TABLE test_unsigned (a INT UNSIGNED, b INT UNSIGNED);
INSERT INTO test_unsigned VALUES(1, 2);
SELECT * FROM test_unsigned;
SELECT b - a FROM test_unsigned;

-- 出错，不允许出现负数
SELECT a - b FROM test_unsigned;
```

    工作中用得最多的应该是 bigint 且 有符号的数

INT(N)是什么？

    这个 n 是在 MySQL 的 console 端 select 的时候显示的长度

```sql
-- zerofill 表示会左填充 0
CREATE TABLE test_int_n (a INT(4) ZEROFILL);

insert into test_int_n values(1);
insert into test_int_n values(123456);

-- 在 MySQL 命令行中 select * 的话，会格式化显示
select * from test_int_n;
```

自动增长的字段

```sql
-- 自动增长的字段必须要加上 primary key
CREATE TABLE test_auto_increment (
	a INT auto_increment PRIMARY KEY
);

-- 分别插入 a 的值为：1(自动增长)、100(指定，且最大值已为100)、101(自动增长)、10(指定)、102(自动增长)
INSERT INTO test_auto_increment VALUES(NULL),(100),(NULL),(10),(NULL);
SELECT * FROM test_auto_increment;

-- 可以插入负数，但不能插入 0
insert into test_auto_increment values(-1);
```

字符、字节


|类型|说明|N的含义|是否有字符集|最大长度|
|:----:|:----:|:----:|:----:|:----:|
|char(n)|定长字符|字符|是|255|
|varchar(n)|变长字符|字符|是|16384|
|binary(n)|定长二进制字节|字节|否|255|
|varbinary(n)|变长二进制字节|字节|否|16384|
|tinyblob(n)|二进制大对象|字节|否|256|
|blob(n)|二进制大对象|字节|否|16K|
|mediumblob(n)|二进制大对象|字节|否|16M|
|longblob(n)|二进制大对象|字节|否|4G|
|tinytext(n)|大对象|字节|是|256|
|text(n)|大对象|字节|是|16K|
|mediumtext(n)|大对象|字节|是|16M|
|longtext(n)|大对象|字节|是|4G|

```sql
-- 字符大小写比较
SELECT 'a' = 'A'; -- 返回 1

create table test_ci (a varchar(10), key(a));
insert into test_ci values('a');
insert into test_ci values('A');

-- 字符大小写不敏感
select * from test_ci where a = 'a';

-- 设置排序规则为 bin 方式
set names utf8mb4 collate utf8mb4_bin;
SELECT 'a' = 'A'; -- 此时会返回 0
```

时间类型

|日期类型|占用空间|表示范围|
|:----------|:---:|:----------|
|DATETIME|8|1000-01-01 00:00:00 ~ 9999-12-31 23:59:59|
|DATE|3|1000-01-01 ~ 9999-12-31|
|TIMESTAMP|4|1970-01-01 00:00:00UTC ~ 2038-01-19 03:14:07UTC|
|YEAR|1|YEAR(2): 1970-2070, YEAR(4):1901-2155|
|TIME|3|-838:59:59 ~ 838:59:59|


```sql
create table test_time (a TIMESTAMP, b datetime);
insert into test_time values(NOW(),NOW());

-- a 的时间跟 b 的时间一样，时区没有设置
select * from test_time;

-- 设置时区
select @@time_zone; -- 默认使用 SYSTEM 时间
SET time_zone='+00:00'; -- 时区设为
select * from test_time; -- 此时 a 和 b 的值不一样了，timestamp带有时区
```


### JSON类型(MySQL 5.7才有)

```sql
CREATE TABLE json_user (
	uid INT auto_increment,
	data json,
	PRIMARY KEY (uid)
);

INSERT INTO json_user
VALUES(
		NULL,
		'{"name": "lison", "age": 10, "address": "enjoy"}'
);

INSERT INTO json_user
VALUES(
		NULL,
		'{"name": "james", "age": 20, "mail": "james@163.com"}'
);

```

```text
mysql> select * from json_user;
+-----+-------------------------------------------------------+
| uid | data                                                  |
+-----+-------------------------------------------------------+
|   1 | {"age": 10, "name": "lison", "address": "enjoy"}      |
|   2 | {"age": 20, "mail": "james@163.com", "name": "james"} |
+-----+-------------------------------------------------------+
2 rows in set (0.00 sec)
```

1、 json_extract 抽取

```text
mysql> select json_extract('[10, 20, [30, 40]]', '$[1]');
+--------------------------------------------+
| json_extract('[10, 20, [30, 40]]', '$[1]') |
+--------------------------------------------+
| 20                                         |
+--------------------------------------------+
1 row in set (0.00 sec)
```

```text
mysql> SELECT json_extract(`data`, '$.name'),json_extract(`data`, '$.address') FROM json_user;
+--------------------------------+-----------------------------------+
| json_extract(`data`, '$.name') | json_extract(`data`, '$.address') |
+--------------------------------+-----------------------------------+
| "lison"                        | "enjoy"                           |
| "james"                        | NULL                              |
+--------------------------------+-----------------------------------+
2 rows in set (0.01 sec)
```

2、 JSON_OBJECT

json_object 两两匹配成 json 对象

```text
mysql> SELECT json_object("name", "enjoy", "email", "enjoy.com", "age", 11);
+------------------------------------------------------------+
| json_object("name", "enjoy", "email", "enjoy.com", "age", 11) |
+------------------------------------------------------------+
| {"age": 11, "name": "enjoy", "email": "enjoy.com"}         |
+------------------------------------------------------------+
1 row in set (0.00 sec)
```

```sql
-- 插入 json_object 到数据表中
INSERT INTO json_user VALUES(
	NULL,
	json_object("name", "enjoy", "email", "enjoy.com", "age", 11)
);
```

```text
mysql> select * from json_user;
+-----+-------------------------------------------------------+
| uid | data                                                  |
+-----+-------------------------------------------------------+
|   1 | {"age": 10, "name": "lison", "address": "enjoy"}      |
|   2 | {"age": 20, "mail": "james@163.com", "name": "james"} |
|   3 | {"age": 11, "name": "enjoy", "email": "enjoy.com"}    |
+-----+-------------------------------------------------------+
3 rows in set (0.00 sec)
```

3、 json_insert

插入一个json对象，如果原对象中已有属性，则不会改变其属性值，反之则插入新属性

```sql
SHOW VARIABLES LIKE '%datadir%';
SELECT @@datadir;
```

```text
mysql> SET @json = '{ "a": 1, "b": [2, 3]}';
Query OK, 0 rows affected (0.02 sec)

mysql> SELECT json_insert(@json, '$.a', 10, '$.c', '[true, false]');
+-------------------------------------------------------+
| json_insert(@json, '$.a', 10, '$.c', '[true, false]') |
+-------------------------------------------------------+
| {"a": 1, "b": [2, 3], "c": "[true, false]"}           |
+-------------------------------------------------------+
1 row in set (0.02 sec)
```

```text
mysql> UPDATE json_user SET data = json_insert(data,"$.address_2", "xiangxue") where uid = 1;
Query OK, 1 row affected (0.13 sec)
Rows matched: 1  Changed: 1  Warnings: 0

mysql> select * from json_user;
+-----+---------------------------------------------------------------------------+
| uid | data                                                                      |
+-----+---------------------------------------------------------------------------+
|   1 | {"age": 10, "name": "lison", "address": "enjoy", "address_2": "xiangxue"} |
|   2 | {"age": 20, "mail": "james@163.com", "name": "james"}                     |
|   3 | {"age": 11, "name": "enjoy", "email": "enjoy.com"}                        |
+-----+---------------------------------------------------------------------------+
3 rows in set (0.00 sec)
```

4、 json_merge 合并两个json对象为一个

```text
mysql> select json_merge('{"name": "enjoy"}', '{"id": 47}');
+-----------------------------------------------+
| json_merge('{"name": "enjoy"}', '{"id": 47}') |
+-----------------------------------------------+
| {"id": 47, "name": "enjoy"}                   |
+-----------------------------------------------+
1 row in set, 1 warning (0.36 sec)
```

```text
// 如果有相同的属性名，则会将其合并成一个数组
mysql> select json_merge('{"id": "enjoy"}', '{"id": 47}');
+---------------------------------------------+
| json_merge('{"id": "enjoy"}', '{"id": 47}') |
+---------------------------------------------+
| {"id": ["enjoy", 47]}                       |
+---------------------------------------------+
1 row in set, 1 warning (0.00 sec)
```

```text
mysql> select json_merge(
    -> json_extract(data, '$.address'),
    -> json_extract(data, '$.address_2'))
    -> from json_user where uid = 1;
+---------------------------------------------------------------------------------+
| json_merge(
json_extract(data, '$.address'),
json_extract(data, '$.address_2')) |
+---------------------------------------------------------------------------------+
| ["enjoy", "xiangxue"]                                                           |
+---------------------------------------------------------------------------------+
1 row in set, 1 warning (0.02 sec)
```

5、json类型无法直接创建索引，但可以间接的创建索引，使用虚拟列

```sql
-- 虚拟列的语法一定死记
CREATE TABLE test_index_1 (
	data json,
	-- 这里多加了 json_unquote 方法来去掉结果中的双引号
	gen_col VARCHAR(10) generated always as (json_unquote(json_extract(data, '$.name'))),
	-- 这里的结果会包含json特有的双引号，会导致select的时候要多加一层单引号来包含双引号，见下面例子
	-- gen_col VARCHAR(10) generated always as (json_extract(data, '$.name')),
	INDEX idx(gen_col)
);

INSERT INTO test_index_1(data) VALUES('{"name": "king", "age": 18, "address": "cs"}');
INSERT INTO test_index_1(data) VALUES('{"name": "peter", "age": 28, "address": "zz"}');
-- 查询的时候，where 语句需要带上双引号，gen_col里面的值是带引号的属性值，如 "king"/"peter"
```

```text
mysql> select * from test_index_1;
+-----------------------------------------------+---------+
| data                                          | gen_col |
+-----------------------------------------------+---------+
| {"age": 18, "name": "king", "address": "cs"}  | "king"  |
| {"age": 28, "name": "peter", "address": "zz"} | "peter" |
+-----------------------------------------------+---------+
2 rows in set (0.00 sec)

## 错误的查询方式
mysql> select json_extract(data, "$.name") as username from test_index_1 where gen_col = "king";
Empty set (0.02 sec)

## 正确的查询方式
mysql> select json_extract(data, "$.name") as username from test_index_1 where gen_col = '"king"';
+----------+
| username |
+----------+
| "king"   |
+----------+
1 row in set (0.00 sec)
```

虚拟列去掉json的双引号

```sql
CREATE TABLE test_index_2(
	data json,
	gen_col VARCHAR(10) generated always as (json_unquote(json_extract(data, '$.name'))),
	INDEX idx(gen_col)
);
```

```text
mysql> select * from test_index_2;
+-----------------------------------------------+---------+
| data                                          | gen_col |
+-----------------------------------------------+---------+
| {"age": 18, "name": "king", "address": "cs"}  | king    |
| {"age": 28, "name": "peter", "address": "zz"} | peter   |
+-----------------------------------------------+---------+
2 rows in set (0.33 sec)

mysql> select json_extract(data, "$.name") as username from test_index_2 where gen_col = "king";
+----------+
| username |
+----------+
| "king"   |
+----------+
1 row in set (0.00 sec)

mysql> select json_extract(data, "$.name") as username from test_index_2 where gen_col = 'king';
+----------+
| username |
+----------+
| "king"   |
+----------+
1 row in set (0.01 sec)
```