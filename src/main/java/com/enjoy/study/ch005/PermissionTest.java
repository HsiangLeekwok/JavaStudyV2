package com.enjoy.study.ch005;

import org.junit.Test;

import java.util.concurrent.BlockingDeque;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/07 19:18<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class PermissionTest {

    public static final int INSERT = 1 << 0;
    public static final int SELECT = 1 << 1;
    public static final int UPDATE = 1 << 2;
    public static final int DELETE = 1 << 3;

    private int flag;

    public void setPermission(int permission) {
        flag = permission;
    }

    public void addPermission(int permission) {
        flag |= permission;
    }

    public boolean hasPermission(int permission) {
        return (flag & permission) == permission;
    }

    public void removePermission(int permission) {
        flag = flag & ~permission;
    }

    public void permissions() {
        System.out.println("INSERT: " + hasPermission(INSERT) + ", SELECT: " + hasPermission(SELECT) +
                ", UPDATE: " + hasPermission(UPDATE) + ", DELETE: " + hasPermission(DELETE));
    }

    @Test
    public void test() {
        int flag = 15;
        PermissionTest test = new PermissionTest();
        test.setPermission(flag);
        test.permissions();
        test.removePermission(SELECT | UPDATE);
        test.permissions();
    }
}
