package com.enjoy.study.season07_Zookeeper.ch02_Test;

import org.apache.zookeeper.*;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/08/15 20:02<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: <br/>
 * <b>Description</b>:
 */
public class ZookeeperTest {

    // zk服务器地址
    private static final String SERVER = "127.0.0.1:2181";
    // zk超时时间
    private static final int TIMEOUT = 30000;

    @Test
    public void testSession() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(SERVER, TIMEOUT, null);
        System.out.println(zooKeeper);
        System.out.println(zooKeeper.getState());

        // 创建节点，此时可能会不成功，因为链接还未连接成功
        zooKeeper.create("/king", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    private CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testSession1() throws Exception {
        ZooKeeper zooKeeper = new ZooKeeper(SERVER, TIMEOUT, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("connect ok");
                latch.countDown();
            }
        });

        latch.await();

        zooKeeper.create("/king", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }
}
