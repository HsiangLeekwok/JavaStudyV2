package com.enjoy.study.season03_MySQL;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/03 16:34<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 批量导入测试<br/>
 * <b>Description</b>: 导入100万条数据，查看效率如何
 */
public class InsertDemo {

    private static String user = "root";
    private static String pass = "root1234%";
    private static String URL = "jdbc:mysql://127.0.0.1:3306/demo";

    @Test
    public void test1() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("D:\\sql.sql"));
        Connection connection = DriverManager.getConnection(URL, user, pass);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        br.lines().forEach(sql -> {
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.executeUpdate();
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        br.close();
        connection.close();

        LocalDateTime now2 = LocalDateTime.now();
        System.out.println(now2);
    }

    private int i = 0;

    @Test
    public void test2() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("D:\\sql.sql"));
        Connection connection = DriverManager.getConnection(URL, user, pass);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        // 设置不自动提交
        connection.setAutoCommit(false);

        br.lines().forEach(sql -> {
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.addBatch();
                if ((i % 2000 != 0) && i < 2097152) {
                    i++;
                } else {
                    ps.executeBatch();
                    connection.commit();
                    i = 0;
                }
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        br.close();
        connection.close();

        LocalDateTime now2 = LocalDateTime.now();
        System.out.println(now2);
    }

    private String str = "INSERT INTO `product_info` VALUES ";

    @Test
    public void test3() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("D:\\sql.sql"));
        Connection connection = DriverManager.getConnection(URL, user, pass);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        // 不需要自动提交
        connection.setAutoCommit(false);
        br.lines().forEach(sql -> {
            try {
                str = str + sql.split("VALUES")[1].replace(";", ",");
                if (i % 2000 != 0 && i < 2097152) {
                    i++;
                } else {
                    i++;
                    str = str.substring(0, str.length() - 1);
                    PreparedStatement ps = connection.prepareStatement(str);
                    ps.executeUpdate();
                    str = "INSERT INTO `product_info` VALUES ";
                    connection.commit();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        br.close();
        connection.close();

        LocalDateTime now2 = LocalDateTime.now();
        System.out.println(now2);
    }
}
