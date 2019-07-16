package com.enjoy.study.season04_Netty.ch02.rpc.register;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <b>Author</b>: 小果<br/>
 * <b>Date</b>: 2019/07/08 23:42<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: rpc 的注册中心<br/>
 * <b>Description</b>:
 */
public class RegisterCenter {

    /**
     * 线程池负责处理所有的注册、查询请求
     */
    private ExecutorService serviceHandler = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    /**
     * 服务和提供这个服务的地址列表(可能有多个服务节点)
     */
    private ConcurrentHashMap<String, Set<ServicePoint>> services = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, Integer> callTimes = new ConcurrentHashMap<>();

    private int port;

    private RegisterCenter(int port) {
        this.port = port;
    }

    /**
     * 注册服务，单点服务，没考虑多个服务节点的问题
     *
     * @param serviceName
     * @param host
     * @param port
     */
    private void registerService(String serviceName, String host, int port) {
        Set<ServicePoint> pointSet = services.get(serviceName);
        if (null == pointSet) {
            pointSet = new HashSet<>();
            services.put(serviceName, pointSet);
            // 调用次数为0
            callTimes.put(serviceName, 0);
        }

        // ServerPoint 重写了 equals 方法以判断相同的 host 和 port，以此避免重复注册
        pointSet.add(new ServicePoint(host, port));
        System.out.println("Service [" + serviceName + ", " + host + ":" + port + "] has registered.");
    }

    /**
     * 查找服务
     *
     * @param serviceName
     * @return
     */
    private Set<ServicePoint> getService(String serviceName) {
        return services.get(serviceName);
    }

    /**
     * 统计 rpc 查询次数
     *
     * @param serviceName
     * @return
     */
    private int countService(String serviceName) {
        return callTimes.get(serviceName);
    }

    /**
     * 服务处理类
     */
    private class ServiceHandlerTask implements Runnable {
        private Socket socket = null;

        public ServiceHandlerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // 输出流
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                // 输入流
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

                // 读取注册标记
                byte flag = input.readByte();
                String serviceName = input.readUTF();
                switch (flag) {
                    case -1:
                        // 注册新服务节点
                        String host = input.readUTF();
                        int port = input.readInt();
                        registerService(serviceName, host, port);

                        // 注册成功，返回成功标记给客户端
                        output.writeBoolean(true);
                        break;
                    case 0:
                        // 查询已注册的 rpc 服务
                        Set<ServicePoint> points = getService(serviceName);
                        if (null == points || points.size() < 1) {
                            output.writeObject(points);
                        } else {
                            // 查询次数
                            int times = callTimes.get(serviceName);
                            ServicePoint[] all = new ServicePoint[points.size()];
                            points.toArray(all);
                            // 模拟多个 rpc 服务的平均调用
                            ServicePoint point = all[times % all.length];

                            // 次数 +1，下一次再查询后会自动调整到另外一台 rpc 服务器上
                            callTimes.put(serviceName, times + 1);

                            // 查询结果返回给客户端
                            output.writeObject(point);
                        }
                        break;
                    case 1:
                        int calls = countService(serviceName);
                        output.writeInt(calls);
                        break;
                    default:
                        output.writeUTF("not support request of: " + flag);
                        break;
                }
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动注册中心的服务
     *
     * @throws Exception
     */
    public void startService() throws Exception {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port));
        System.out.println("Register center is listen on " + port);
        try {
            while (true) {
                serviceHandler.execute(new ServiceHandlerTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                RegisterCenter center = new RegisterCenter(10086);
                center.startService();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }
}
