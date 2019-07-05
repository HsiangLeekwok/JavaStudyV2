package com.enjoy.study.season04_Netty.ch02.bio;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/05 22:36<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: BIO 通讯客户端<br/>
 * <b>Description</b>: 通过 BIO 方式实现客户端
 */
public class Client {

    public static void main(String[] args) throws Exception {
        // 客户端启动必备
        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        // 对端地址和端口号
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10001);
        try {
            socket = new Socket();
            // 三次握手连接对端
            socket.connect(address);

            // 拿到输入输出流
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            outputStream.writeUTF("Leekwok");
            outputStream.flush();

            System.out.println(inputStream.readUTF());
        } finally {
            if (null != socket) socket.close();
            if (null != inputStream) inputStream.close();
            if (null != outputStream) outputStream.close();
        }
    }
}
