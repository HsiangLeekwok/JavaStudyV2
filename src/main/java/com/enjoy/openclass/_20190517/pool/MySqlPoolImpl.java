package com.enjoy.openclass._20190517.pool;

import java.sql.Connection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/05/17 20:46<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: MySQL 数据库连接池实现类 <br/>
 * <b>Description</b>:
 */
public class MySqlPoolImpl implements MySqlPool {

    /**
     * 最大连接数量
     */
    private final int maxSize = 10;
    /**
     * 当前已有的连接数量
     */
    private AtomicInteger activeSize = new AtomicInteger(0);

    /**
     * 空闲连接
     */
    private LinkedBlockingQueue<Connection> idle;
    /**
     * 已被外界取走使用的连接列表
     */
    private LinkedBlockingQueue<Connection> busy;

    @Override
    public void init() {
        idle = new LinkedBlockingQueue<>();
        busy = new LinkedBlockingQueue<>();
    }

    @Override
    public void destroy() {

    }

    @Override
    public Connection getConnection() {
        // Idle 有值，直接取
        Connection connection = idle.poll();
        if (null != connection) {
            busy.offer(connection);
            return connection;
        }
        // Idle无值，若池子未满，新建连接
        if (activeSize.get() < maxSize) {
            //原子的双重判断
            if (activeSize.incrementAndGet() <= maxSize) {
                // 如果池子中数量小于最大值
                //connection = CreateConnection();
                activeSize.incrementAndGet();
                busy.offer(connection);
                return connection;
            }
        }
        // 池满全繁忙，阻塞等待返还
        try {
            connection = idle.poll(10000, TimeUnit.MILLISECONDS);
            if (null == connection) {
                throw new RuntimeException("wait connection timeout.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void release(Connection connection) {
        busy.remove(connection);
        idle.offer(connection);
    }
}
