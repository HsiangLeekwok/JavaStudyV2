package com.enjoy.study.season04_Netty.ch02.rpc.client.frame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/08 21:28<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Rpc框架的客户端代理部分<br/>
 * <b>Description</b>:
 */
public class RpcClientFrame {

    // 远程代理对象
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteProxyObject(final Class<?> serviceInterface, String host, int port) {
        final InetSocketAddress address = new InetSocketAddress(host, port);
        return (T) Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[]{serviceInterface}, new DynProxy(serviceInterface, address));
    }

    private static class DynProxy implements InvocationHandler {

        private final Class<?> serviceInterface;
        private final InetSocketAddress address;

        public DynProxy(Class<?> serviceInterface, InetSocketAddress address) {
            this.serviceInterface = serviceInterface;
            this.address = address;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Socket socket = null;
            ObjectOutputStream output = null;
            ObjectInputStream input = null;
            try {
                socket = new Socket();
                socket.connect(address);

                output = new ObjectOutputStream(socket.getOutputStream());
                // 方法所在的类
                output.writeUTF(serviceInterface.getName());
                // 方法的名称
                output.writeUTF(method.getName());
                Class<?>[] params = method.getParameterTypes();
                output.writeObject(params);
                output.writeObject(args);
                output.flush();

                input = new ObjectInputStream(socket.getInputStream());
                return input.readObject();
            } finally {
                if (socket != null) {
                    socket.close();
                }
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            }
        }
    }

}
