package com.enjoy.study.season04_Netty.ch02.rpc.server.frame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/07/08 21:42<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: rpc框架服务端部分<br/>
 * <b>Description</b>:
 */
public class RpcServerFrame {

    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    // 服务的注册中心
    private static final Map<String, Class> serviceHolder = new HashMap<>();

    private int port;

    public RpcServerFrame(int port) {
        this.port = port;
    }

    public void startService() throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("RPC server is listen on: " + port + "");
        try {
            while (true) {
                executorService.execute(new ServerTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    public void registerService(String className, Class impl) {
        serviceHolder.put(className, impl);
    }

    private static class ServerTask implements Runnable {

        private Socket client = null;

        public ServerTask(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());

                // 方法所在的类的接口名称
                String serviceName = inputStream.readUTF();
                // 方法名
                String methodName = inputStream.readUTF();
                // 方法参数列表的类型
                Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
                // 方法参数的值
                Object[] args = (Object[]) inputStream.readObject();

                // 反射获取类实例和方法名，并调用方法获取结果
                Class serviceClass = serviceHolder.get(serviceName);
                if (null == serviceClass) {
                    throw new ClassNotFoundException("class: " + serviceName + " not found");
                }
                Method method = serviceClass.getMethod(methodName, paramTypes);
                if (null == method) {
                    throw new Exception("method: " + methodName + " not found");
                }

                // 反射获取方法的结果
                Object result = method.invoke(serviceClass.newInstance(), args);
                // 结果返回到客户端
                outputStream.writeObject(result);
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
