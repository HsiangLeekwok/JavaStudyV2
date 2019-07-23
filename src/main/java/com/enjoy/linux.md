docker 基础

显示当前运行信息
docker ps

显示 images 信息
docker images

挂载 image
docker load --input /shared/images/cent6base.tar

启动一个新容器并将宿主机的20022端口绑定到容器的22端口上
docker run -d -v /shared:/shared -p 20022:22 --name test1 cent6:base

停止一个容器
docker stop test1

删除一个容器
docker rm test1










Linux 基础

查看操作系统版本
cat /etc/centos-release

1、查看当前目录下的内容类似dir
ls或ll

	查看根目录下的所有内容：ls /
	
2、进入目录

	cd /shared	// 以斜杠开头的表示绝对路径
	cd ..  // 返回上一层目录
	
3、wget到网络上下载内容

4、curl www.baidu.com拉取baidu首页的html页面

5、history：本次命令历史

	history | grep cd 显示历史记录并且只显示含有cd的命令
	
6、安装软件

	1、rpm -ivh jdk-....rpm
	-i install	// 安装软件
	-v verbos	// 显示安装详细信息
	-h hash		// 安装前验证哈希
	
	2、yum
		yum search infonfig	// 直接搜索当前系统中是否有软件，没有的话就安装
	
7、查看安装的软件被安装到了哪里

	whereis java // 查看java被安装到了哪里
	
8、创建目录和文件

	mkdir aa	// 在当前目录下创建一个名为aa的目录
	touch bb	// 创建一个名为 bb 的文件
	
9、查看文件

	cat bb		// 查看 bb 的内容
		cat bb | grep xxx	// 查看bb的内容并且过滤出来包含有xx的行
		cat bb > cc			// 把 bb 的内容定向传到 cc 文件
		cat bb >> cc 		// 把 bb 的内容追加到 cc 末尾
	more bb 	// 滚动查看 bb 的内容
	tail -20f	// 查看20行
	
10、vi: 
	vi: 刚进入的时候是保护模式，需要敲入 i 才可以输入(insert)
		输入完毕之后按esc键，然后输入:wq
		
		新开一行o
		删除一行dd// 先按2再按dd则删除2行
		
11、文件操作

	cp bb dd	// 复制文件bb到dd
		scp		// 网络复制文件到另外一台机器的指定目录
		scp root@172.17.0.2:/shared/aa/b ./
	mv bb dir/	// 复制文件bb到dir目录
		mv bb dir/ddd	// 移动bb文件到dir目录下并且改名为ddd
	
	du 查看当前文件夹下所有内容的大小，还可以查看文件的大小
		du -sh aa	// 查看aa的大小
		
	df 查看系统磁盘的使用情况
	
	rm 删除命令
	
	文件权限：都有3组rwx，r=读，w=写，x=执行，一般也用777表示，也即7数字的二进制表示rwx
	-rwxrwxrwx
	-表示文件 d表示目录
	rwx	
	rwx
	rwx
	
	修改权限：chmod -x aa 去掉aa文件的执行权限
	
12、用户

	whoami	显示当前使用系统的用户
	pwd		显示当前所在的目录
	useradd test	添加一个名为 test 的用户
	passwd test		给test用户设置密码
	su test	切换用户
	cd ~	进入当前用户的根目录
	
13、查看端口是否被占用

	netstat -nlpt | grep 80
	
14、查看当前内存
	
	free -mkdir
	
15、ps -ef | grep ssh

16、top命令，查看系统资源使用情况

	load average：比较重要

17、kill，杀死进程

18、查找

	find -name aa	// 查找名为 aa的所有文件
find -name bb
./bb
./mm/bb

19、压缩

	gzip aa 压缩aa，会变成aa.gz
	
	tar -zcaf mm.tar mm// 把mm目录打包成mm.tar
	gzip mm.tar// 把mm.tar 压缩成 mm.tar.gz
	tar -zxaf mm.tar.gz 可以直接解压gz压缩的文件
	
20、环境变量设置	