package com.enjoy.study.season04_Netty.ch02.bio;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/05 22:36<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: BIO 服务器端<br/>
 * <b>Description</b>: 实现 BIO 网络通讯
 */
public class Server {

    public static void main(String[] args) throws Exception {
        // 服务器端必备
        ServerSocket serverSocket = new ServerSocket();
        // 绑定监听端口
        serverSocket.bind(new InetSocketAddress(10001));
        System.out.println("Server start.........");

        while (true) {
            // 阻塞方式获取连接进来的客户端
            Socket socket = serverSocket.accept();
            new Thread(new ServerTask(socket)).start();
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
            } finally {

            }
        }
    }

}
