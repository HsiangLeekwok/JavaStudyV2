package com.enjoy.study.season04_Netty.ch02.niodemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 09:07<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: NIO 服务端通讯处理实现<br/>
 * <b>Description</b>:
 */
public class NioServerHandler implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean started;

    public NioServerHandler(int port) {
        try {
            // 创建选择器
            selector = Selector.open();
            // 打开监听通道
            serverChannel = ServerSocketChannel.open();
            // 非阻塞模式
            serverChannel.configureBlocking(false);

            // 绑定端口
            serverChannel.bind(new InetSocketAddress(port));

            // 注册 accept 事件
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            started = true;
            System.out.println("server is started on port " + port);
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
        while (started) {
            try {
                // 阻塞方法，只有有事件发生时才继续进行
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                        if (null != key.channel()) {
                            key.channel().close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                // 处理客户端连接事件
                // 这里获取到的是 ServerSocketChannel
                ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                // 获取客户端的 socket 连接
                SocketChannel accept = channel.accept();
                System.out.println("client connected.........");
                // 设置非阻塞
                accept.configureBlocking(false);
                // 注册读事件
                accept.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                // 处理数据读
                System.out.println("socket channel read.....");
                SocketChannel channel = (SocketChannel) key.channel();
                // 初始化 1k 的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int readBytes = channel.read(buffer);
                if (readBytes > 0) {
                    // 写读转换
                    buffer.flip();
                    byte[] bytes = new byte[buffer.remaining()];
                    // 将ByteBuffer中的数据读取出来
                    buffer.get(bytes);
                    String message = new String(bytes, "UTF-8");
                    System.out.println("Received: " + message);
                    // 处理数据并返回
                    String result = response(message);
                    // 发送应答到客户端
                    doWrite(channel, result);
                } else if (readBytes < 0) {
                    // 连接已关闭
                    System.out.println("socket closed.....");
                    key.cancel();
                    channel.close();
                }
            }
        }
    }

    private String response(String message) {
        return "Hello " + message + ", now is " + new Date(System.currentTimeMillis());
    }

    private void doWrite(SocketChannel channel, String message) throws IOException {
        // 处理数据写事件
        byte[] bytes = message.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer);
    }
}
