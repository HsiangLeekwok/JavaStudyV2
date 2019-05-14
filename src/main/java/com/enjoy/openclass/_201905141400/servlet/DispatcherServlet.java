package com.enjoy.openclass._201905141400.servlet;

import com.enjoy.openclass._201905141400.annotation.TestAutowired;
import com.enjoy.openclass._201905141400.annotation.TestController;
import com.enjoy.openclass._201905141400.annotation.TestRequestMapping;
import com.enjoy.openclass._201905141400.annotation.TestService;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/14 15:27<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class DispatcherServlet {

//    List<String> classNames = new ArrayList<>();
//    // IOC 容器
//    Map<String, Object> beans = new HashMap<>();
//    Map<String, Object> handlerMap = new HashMap<>();
//
//    // 烧毛路径---com.xxxx.OrderService.class
//    // 创建对象，实例化（反射）
//    // autowired
//    // urlhandmappiong，路径映射
//    public void init(ServletConfig config) {
//        doScanBasePackage("");
//        doInstance();
//        doAutowired();
//        urlHanding();
//    }
//
//    private void urlHanding() {
//        // 路径映射
//        for (Map.Entry<String, Object> entry : beans.entrySet()) {
//            Object instance = entry.getValue();
//            Class<?> clazz = instance.getClass();
//            if (clazz.isAnnotationPresent(TestController.class)) {
//                TestRequestMapping mapping = clazz.getAnnotation(TestRequestMapping.class);
//                String classPath = mapping.value();
//
//                // 方法列表
//                Method[] methods = clazz.getMethods();
//                for (Method method : methods) {
//                    if (method.isAnnotationPresent(TestRequestMapping.class)) {
//                        // 方法声明了注解
//                        TestRequestMapping mapping1 = method.getAnnotation(TestRequestMapping.class);
//                        String methodPath = mapping1.value();
//                        String s = classPath + methodPath;
//                        // 方法
//                        handlerMap.put(s, method);
//                    }
//                }
//            }
//        }
//    }
//
//    private void doAutowired() throws IllegalAccessException {
//        // 依赖注入
//        for (Map.Entry<String, Object> entry : beans.entrySet()) {
//            Object instance = entry.getValue();
//            Class<?> clazz = instance.getClass();
//            if (clazz.isAnnotationPresent(TestController.class)) {
//                // 控制类中的依赖注入
//                Field[] fields = clazz.getDeclaredFields();
//                for (Field field : fields) {
//                    if (field.isAnnotationPresent(TestAutowired.class)) {
//                        // 自动注解
//                        TestAutowired autowired = field.getAnnotation(TestAutowired.class);
//                        String key = autowired.value();
//                        Object ins = beans.get(key);
//                        field.setAccessible(true);
//                        field.set(instance, ins);
//                    }
//                }
//            }
//        }
//    }
//
//    private void doInstance() {
//        for (String className : classNames) {
//            // className = com.xxx.xxx.Service.class
//            // 去掉后缀
//            String cn = className.replace(".class", "");
//            // cn = com.xxx.xxx.Service
//            try {
//                Class<?> clazz = Class.forName(cn);
//                // 判断一下当前被遍历的类是不是引用了@Controller、@Service注解
//                if (clazz.isAnnotationPresent(TestController.class)) {
//                    // 控制类 实例化map put key value
//                    Object instance = clazz.newInstance();
//                    // key
//                    TestRequestMapping annotation = clazz.getAnnotation(TestRequestMapping.class);
//                    String key = annotation.value();
//
//                    // key value ----> map
//                    beans.put(key, instance);
//                } else if (clazz.isAnnotationPresent(TestService.class)) {
//                    // 服务类 实例化map put key value
//                    Object instance = clazz.newInstance();
//                    // key
//                    TestService annotation = clazz.getAnnotation(TestService.class);
//                    String key = annotation.value();
//
//                    // key value ----> map
//                    beans.put(key, instance);
//                }
//            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void doScanBasePackage(String basePackage) {
//        URL url = this.getClass().getClassLoader().getResources("/" + basePackage.replaceAll('.', '/'));
//        String fileStr = url.getFile();// fileStr = e:/www/pro/com/xxx/xx
//        File file = new File(fileStr);
//        String[] filesStr = file.list();
//        for (String path : filesStr) {
//            File filePath = new File(filesStr + path);
//            if (filePath.isDirectory()) {
//                // 递归调用扫描子目录
//                doScanBasePackage(basePackage + "." + path);
//            } else {
//                // .class结束的文件
//                // com.xxx.c.TestService.class
//                classNames.add(basePackage + "." + filePath.getName());
//            }
//        }
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
//        this.doPost(request, response);
//    }
//
//    // http://127.0.0.1:8080/xxx/service/query
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
//        String uri = request.getRequestURI();//   /xxx/service/query
//        String context = request.getContextPath();//  /xxx
//        String path = uri.replace(context, "");//  /service/query
//
//        Method method = (Method) handlerMap.get(path);
//        TestController controller = (TestController) beans.get("/" + path.split("/")[1]);
//        Object[] args = hand(request, response, method);
//        try {
//            method.invoke(controller, args);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
}
