package com.enjoy.study.season04_Netty.ch02.bio;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/05 22:54<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 线程池方式实现 BIO 服务端<br/>
 * <b>Description</b>: 服务端增加线程池
 */
public class ServerPool {

    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("Server start...........");
        try {
            while (true) {
                // 向线程池中投递一个执行内容
                pool.execute(new ServerTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    private static class ServerTask implements Runnable {
        private Socket socket = null;

        ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 拿到和客户端通讯的输入和输出流
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                // 进行数据的读取，也即服务器端的输入
                String userName = inputStream.readUTF();
                System.out.println("Accept client message: " + userName);
                // 把数据写入输出缓冲区，还未真正发送到对端
                outputStream.writeUTF("Hello, " + userName);
                // 强制刷新输出缓冲区
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
