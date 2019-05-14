1、war--tomecat---启动的时候：startup.bat，加载war包

war：jar包：写的业务class类：
@Controller
com.cxxxx.OrderController.class

@Service
com.cxxxx.OrderService.class

2、xml basePackage="com.xxxx"" 扫描com.xxx有哪些类声明了特殊注解@Controller、@Service、mybatis、@Component找这些特殊类

3、找到类实例化：创建对象(反射，new一样)
orderController
orderService

4、存储：Map map = new HashMap();// 这个map对象即IOC容器
map.put("orderController", orderController)
map.put("orderService", orderService)

5、依赖处理：orderController——autowired--orderServie--直接到map.get("orderService")

6、http://127.0.0.1:8080/pro/xxx--------controller---method()

7、handlerMapping：解析orderController----/order/query-----method做一个key-value的匹配

tomcat启动成功……Spring框架初始化完毕
访问：http://ip:8080/pro/order/query
SpringMVC----servlet----doGet/doPost

doPost(){
    order/query---method
    执行method：可以调用到method
}



深度：
Spring源码