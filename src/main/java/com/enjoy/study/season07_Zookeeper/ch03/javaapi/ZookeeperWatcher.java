package com.enjoy.study.season07_Zookeeper.ch03.javaapi;

import com.enjoy.utils.ThreadTool;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/08/18 19:17<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 测试zookeeper的watcher机制<br/>
 * <b>Description</b>:
 */
public class ZookeeperWatcher implements Watcher {

    private static final String SERVER = "127.0.0.1:2181";
    private static final int TIMEOUT = 20000;

    /**
     * 父路径
     */
    private static final String PATH = "/watcher";
    /**
     * 子路径
     */
    private static final String CHILD = "/watcher/child";

    private AtomicInteger seq = new AtomicInteger(0);
    private CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk = null;

    private void createConnection(String connectionString, int timeout) {
        releaseConnection();
        try {
            zk = new ZooKeeper(connectionString, timeout, this);
            System.out.println("create connection success....");
            latch.await();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    private void releaseConnection() {
        if (null != zk) {
            try {
                zk.close();
                System.out.println("connection released.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean createPath(String path, String data) {
        try {
            // 设置监控，zookeeper的监控都是一次性的，每次必须要设置监控
            exists(path, true);
            String s = zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("create path '" + path + "' success: " + ", data: " + data);
            return true;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 读取指定节点的内容
     *
     * @param path
     * @return
     */
    private String readData(String path, boolean needWatch) {
        try {
            return new String(zk.getData(path, needWatch, null));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 更新节点
     *
     * @param path
     * @param data
     * @return
     */
    private boolean writeData(String path, String data) {
        try {
            zk.setData(path, data.getBytes(), -1);
            return true;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void deleteNode(String path) {
        try {
            zk.delete(path, -1);
            System.out.println("delete path: '" + path + "' success");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断节点是否存在
     *
     * @param path
     * @param needWatch
     * @return
     */
    private Stat exists(String path, boolean needWatch) {
        try {
            return zk.exists(path, needWatch);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取指定节点的孩子节点列表
     *
     * @param path
     * @param needWath
     * @return
     */
    private List<String> getChildren(String path, boolean needWath) {
        try {
            return zk.getChildren(path, needWath);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除所有节点
     */
    private void deleteAllTestPath() {
        if (null != exists(CHILD, false)) {
            deleteNode(CHILD);
        }
        if (null != exists(PATH, false)) {
            deleteNode(PATH);
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("----------> process event = " + event);

        ThreadTool.sleepMilliseconds(200);

        if (null == event) {
            return;
        }

        // 连接状态
        Event.KeeperState state = event.getState();
        // 事件类型
        Event.EventType type = event.getType();
        // 受影响的path
        String path = event.getPath();
        String logPrefix = "【Watcher - " + seq.incrementAndGet() + "】";

        System.out.println(logPrefix + "收到 Watcher 通知");
        System.out.println(logPrefix + "连接状态：" + state);
        System.out.println(logPrefix + "事件类型：" + type);

        if (Event.KeeperState.SyncConnected == state) {
            switch (type) {
                case None:
                    // 连接成功
                    System.out.println(logPrefix + "成功连接 zk 服务器");
                    latch.countDown();
                    break;
                case NodeCreated:
                    System.out.println(logPrefix + "节点创建成功");
                    ThreadTool.sleepMilliseconds(100);
                    break;
                case NodeDeleted:
                    System.out.println(logPrefix + "节点删除成功");
                    break;
                case NodeDataChanged:
                    System.out.println(logPrefix + "修改节点值");
                    break;
                case NodeChildrenChanged:
                    System.out.println(logPrefix + "子节点数据更改");
                    break;
            }
        } else if (Event.KeeperState.Disconnected == state) {
            System.out.println(logPrefix + "与服务器断开连接");
        } else if (Event.KeeperState.AuthFailed == state) {
            System.out.println(logPrefix + "权限检查失败");
        } else if (Event.KeeperState.Expired == state) {
            System.out.println(logPrefix + "会话超时");
        }
        System.out.println("---------------------------------");
    }

    public static void main(String[] args) {
        ZookeeperWatcher zkWatcher = new ZookeeperWatcher();
        // 连接服务器
        zkWatcher.createConnection(SERVER, TIMEOUT);
        ThreadTool.sleepMilliseconds(1000);
        // 删除测试节点
        zkWatcher.deleteAllTestPath();

        if (zkWatcher.createPath(PATH, System.currentTimeMillis() + "")) {
            System.out.println("-------------- read parent --------------------");
            // 读取数据
            zkWatcher.readData(PATH, true);
            // 写数据
            zkWatcher.writeData(PATH, System.currentTimeMillis() + "");

            System.out.println("-------------------- read child path --------------------------");
            // 读取子节点列表
            zkWatcher.getChildren(PATH, true);

            ThreadTool.sleepMilliseconds(1000);
            // 创建子节点
            zkWatcher.createPath(CHILD, System.currentTimeMillis() + "");
            ThreadTool.sleepMilliseconds(1000);
            // 读子节点
            zkWatcher.readData(CHILD, true);
            zkWatcher.writeData(CHILD, System.currentTimeMillis() + "");
        }

        ThreadTool.sleepMilliseconds(20000);
        // 清理节点
        zkWatcher.deleteAllTestPath();

        ThreadTool.sleepMilliseconds(1000);
        // 释放连接
        zkWatcher.releaseConnection();
    }

    private int cnt = 0;

    @Test
    public void test() {
        int i;
        i = x(x(8));
        System.out.println("times: " + cnt);
    }

    private int x(int n) {
        cnt++;
        if (n <= 3) {
            return 1;
        } else {
            return x(n - 2) + x(n - 4) + 1;
        }
    }
}
