package com.enjoy.study.season04_Netty.ch02.niodemo;

import sun.nio.ch.SelectionKeyImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 09:06<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: NIO 客户端 handler 实现<br/>
 * <b>Description</b>:
 */
public class NioClientHandle implements Runnable {

    private String host;
    private int port;
    private volatile boolean started;
    private Selector selector;
    private SocketChannel socketChannel;

    public NioClientHandle(String host, int port) {
        this.host = host;
        this.port = port;
        // 创建选择器
        try {
            // 创建选择器
            this.selector = Selector.open();
            // 打开监听通道
            socketChannel = SocketChannel.open();
            // 开启非阻塞模式，缺省为 true，阻塞模式
            // true=阻塞模式
            // false=非阻塞模式
            socketChannel.configureBlocking(false);
            started = true;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        while (started) {
            try {
                // select是个阻塞方法，至少有一个事件时才会继续
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    // 读取所有key
                    key = iterator.next();
                    // SelectionKey处理过后要从集合中删除，如果不删除，下次select的时候还会出来
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (null != key) {
                            // 如果当前处理 key 的事件时发生异常，则取消当前关注事件
                            key.cancel();
                            if (null != key.channel()) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        // 关闭 selector
        if (null != selector) {
            try {
                // 关闭选择器，并取消选择器上所有关注的事件
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            // 获得当前关心时间的 channel
            SocketChannel channel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                // 处理连接事件，但3
                if (channel.finishConnect()) {
                    // 三次握手成功了，注册读时间
                    // register会覆盖之前关注的事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else {
                    System.out.println("connect failed, exit.....");
                    System.exit(-1);
                }
            }
            if (key.isReadable()) {
                // 读事件
                // 创建 ByteBuffer，并开辟 1k 的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 从 channel 把数据写入 byteBuffer 缓冲区
                int readBytes = channel.read(buffer);
                if (readBytes > 0) {
                    // buffer的读写模式切换
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    // 将 buffer 中的数据填充到 bytes 数组中
                    buffer.get(bytes);
                    String result = new String(bytes, "UTF-8");
                    System.out.println("Client recv msg: " + result);
                } else if (readBytes < 0) {
                    // 链路已关闭，释放资源
                    key.cancel();
                    channel.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        // 如果此通道处于非阻塞模式，则调用此方法启动非阻塞操作
        // 如果连接码上建立成功，则此方法返回true，否则返回false
        // 因此必须关注连接就绪事件，并通过finishConnect方法完成连接操作
        if (socketChannel.connect(new InetSocketAddress(host, port))) {

        } else {
            // 连接没有马上成功，还处在3此握手阶段，则需要注册 connect 事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    // 向外暴露的写数据方法
    public void sendMessage(String message) throws IOException {
        doWrite(socketChannel, message);
    }

    private void doWrite(SocketChannel channel, String message) throws IOException {
        // 获取要发送的数据的字节数组
        byte[] bytes = message.getBytes();
        // 定义合适的写缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        // 将字节数组放入写缓冲区
        writeBuffer.put(bytes);
        // 缓冲区读写转换
        writeBuffer.flip();
        // 把缓冲区的数据读入channel
        channel.write(writeBuffer);
    }
}
