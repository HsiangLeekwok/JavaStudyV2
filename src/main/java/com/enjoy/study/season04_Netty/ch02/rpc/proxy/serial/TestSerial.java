package com.enjoy.study.season04_Netty.ch02.rpc.proxy.serial;

import javax.swing.text.html.HTMLDocument;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:02<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class TestSerial {

    public static void main(String[] args) {
        // 创建一个 userInfo 对象
//        UserInfo user = new UserInfo("Leekwok", "12345");
//        // 创建一个list对象
//        List<String> list = new ArrayList<>();
//        list.add("My name ");
//        list.add("is ");
//        list.add("Leekwok");
//        try {
//            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("D:/serial.txt"));
//            os.writeObject(user);
//            os.writeObject(list);
//            os.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
        // 读取文件中的 object
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("D:/serial.txt"));
            UserInfo temp = (UserInfo) is.readObject();
            System.out.println("phone: " + temp.getPhone());
            System.out.println("name: " + temp.getName());
            List tempList = (List) is.readObject();
            for (Object o : tempList) {
                System.out.println(o);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
