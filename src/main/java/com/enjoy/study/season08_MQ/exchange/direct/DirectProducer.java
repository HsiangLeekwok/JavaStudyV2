package com.enjoy.study.season08_MQ.exchange.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/19 20:19<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: RabbitMQ Direct Producer 直接交换器的消息生产者<br/>
 * <b>Description</b>:
 */
public class DirectProducer {

    final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        // 创建链接、链接到RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置一下链接工厂的链接地址，默认端口是5672
        connectionFactory.setHost("localhost");
        // 创建链接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 在信道中声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 声明队列(可以放到消费者中去做)
        // 声明路由键和消息体
        String[] routeKeys = {"king", "mark", "james"};
        for (int i = 0; i < 6; i++) {
            String routeKey = routeKeys[i % 3];
            String msg = "Hello, RabbitMQ " + (i + 1);
            // 发布消息
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, msg.getBytes());
            System.out.println("Sent: " + routeKey + ", msg: " + msg);
        }
        // 关闭信道
        channel.close();
        // 关闭连接
        connection.close();
    }
}
