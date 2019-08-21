package com.enjoy.study.season07_Zookeeper.ch03.javaapi;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/08/15 21:05<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Zookeeper原生api测试<br/>
 * <b>Description</b>:
 */
public class TestJavaApi implements Watcher {

    private static final String SERVER = "127.0.0.1:2181";
    private static final int TIMEOUT = 20000;

    private static final String ZK_PATH = "/leader";

    private ZooKeeper zk;
    private CountDownLatch latch = new CountDownLatch(1);
    private AtomicInteger inc = new AtomicInteger(0);

    private void createConnection(String connectStr, int sessionTimeout) {
        this.closeConnection();
        try {
            zk = new ZooKeeper(connectStr, sessionTimeout, this);
            latch.await();
        } catch (IOException | InterruptedException e) {
            System.out.println("Create connect failed.");
            e.printStackTrace();
        }
    }

    public boolean createPath(String path, String data) {
        try {
            System.out.println("创建节点，path：" + path
                    + zk.create(path, data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL) + ", content: " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除指定节点
     *
     * @param path
     * @return
     */
    private void deletePath(String path) {
        try {
            zk.delete(path, -1);
            System.out.println("delete path '" + path + "' success.");
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    private String readData(String path) {
        String result = null;
        try {
            result = new String(zk.getData(path, false, null));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 更新指定节点的内容
     *
     * @param path 节点路径
     * @param data 内容
     * @return
     */
    private boolean writeData(String path, String data) {
        try {
            Stat stat = zk.setData(path, data.getBytes(), -1);
            System.out.println("write data to '" + path + "' success, status: " + stat);
            return true;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭客户端
     */
    public void closeConnection() {
        if (null != zk) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("watched event process ++++++++++ " + inc.incrementAndGet());
        System.out.println("path: " + event.getPath());
        System.out.println("type: " + event.getType());
        System.out.println("stat: " + event.getState());
        if (Event.KeeperState.SyncConnected == event.getState()) {
            latch.countDown();
        }
    }

    public static void main(String[] args) {
        TestJavaApi sample = new TestJavaApi();
        sample.createConnection(SERVER, TIMEOUT);
        if (sample.createPath(ZK_PATH, "I'm the initialized value.")) {
            System.out.println();
            System.out.println("read data: " + sample.readData(ZK_PATH) + "\n");
            sample.writeData(ZK_PATH, "I'm the updated value.");
            System.out.println("read data: " + sample.readData(ZK_PATH) + "\n");
            sample.deletePath(ZK_PATH);
        }

        sample.closeConnection();
    }
}
