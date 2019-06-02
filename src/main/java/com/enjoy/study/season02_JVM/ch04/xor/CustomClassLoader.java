package com.enjoy.study.season02_JVM.ch04.xor;

import java.io.File;
import java.io.FileInputStream;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/06/02 22:06<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 自定义的类加载器进行异或解密<br/>
 * <b>Description</b>:
 */
public class CustomClassLoader extends ClassLoader {

    private final String name;
    private String basePath;

    public CustomClassLoader(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return "CustomClassLoader{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * 设置类的加载目录
     *
     * @param path 本地路径
     */
    public void setBasePath(String path) {
        this.basePath = path;
    }

    private byte[] loadClassData(String name) {
        byte[] data = null;
        XorEncrypt encrypt = new XorEncrypt();
        try {
            String tempName = name.replaceAll("\\.", "\\\\");
            File file = new File(basePath + "\\" + tempName + ".class");
            data = encrypt.decrypt(file);
            //FileInputStream fis = new FileInputStream(file);
            //data = new byte[(int) file.length()];
            //int read = fis.read(data, 0, data.length);//encrypt.decrypt(new File(basePath + "\\" + tempName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] data = this.loadClassData(name);
        return defineClass(null, data, 0, data.length);
    }
}
