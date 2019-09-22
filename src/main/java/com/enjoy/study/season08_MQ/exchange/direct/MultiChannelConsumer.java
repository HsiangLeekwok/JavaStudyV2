package com.enjoy.study.season08_MQ.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/22 11:50<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 一个连接多个信道<br/>
 * <b>Description</b>:
 */
public class MultiChannelConsumer {

    private static class ConsumerWorker implements Runnable {

        final Connection connection;

        public ConsumerWorker(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                // 创建一个信道，也即一个线程一个信道
                Channel channel = connection.createChannel();
                // 声明交换器
                channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                // 声明一个随机队列
                String queueName = channel.queueDeclare().getQueue();

                // 消费者名字
                final String consumerName = Thread.currentThread().getName() + "-all";
                // 队列绑定到交换器上，绑定多个路由
                String[] routeKeys = {"king", "mark", "james"};
                for (String routeKey : routeKeys) {
                    channel.queueBind(queueName, DirectProducer.EXCHANGE_NAME, routeKey);
                }
                System.out.println("[" + consumerName + "] Waiting for message....");

                // 声明一个消费者
                final Consumer consumer = new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String message = new String(body, StandardCharsets.UTF_8);
                        System.out.println("Received [" + envelope.getRoutingKey() + "]: " + message);
                    }
                };

                // 消费者/队列开始消费，并设置自动ack
                channel.basicConsume(queueName, true, consumer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置连接工厂的服务地址
        factory.setHost("localhost");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建多个信道并绑定多个路由键
        for (int i = 0; i < 2; i++) {
            Thread thread = new Thread(new ConsumerWorker(connection));
            thread.start();
        }
    }
}
