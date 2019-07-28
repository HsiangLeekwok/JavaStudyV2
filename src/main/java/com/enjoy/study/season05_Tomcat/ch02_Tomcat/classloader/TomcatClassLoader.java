package com.enjoy.study.season05_Tomcat.ch02_Tomcat.classloader;

import com.sun.nio.zipfs.ZipPath;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/23 20:26<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class TomcatClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        HashMap hashMap = new HashMap();
        System.out.println("HashMap的类加载器：" + HashMap.class.getClassLoader());

        System.out.println("ZipPath的类加载器：" + ZipPath.class.getClassLoader());
        System.out.println("TomcatClassLoader 的类加载器：" + TomcatClassLoader.class.getClassLoader());

        TomcatClassLoader loader = new TomcatClassLoader();
        Object obj = loader.loadClass("com.enjoy.study.season05_Tomcat.ch02_Tomcat.classloader.TomcatClassLoader").newInstance();
        System.out.println("obj: " + obj.getClass().getClassLoader());
        System.out.println("两个是否相等: " + (obj instanceof TomcatClassLoader));
        //System.out.println(obj == loader);
    }

    // 打破双亲委派必须重写loadClass方法
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
            InputStream is = getClass().getResourceAsStream(fileName);
            if (is == null) {
                return super.loadClass(name);
            }
            byte[] b = new byte[is.available()];
            is.read(b);
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name);
        }
    }

    public static void classPath() {

    }
}
