package com.enjoy.study.season08_MQ.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/19 20:19<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Rabbit MQ Normal Consumer 消费者<br/>
 * <b>Description</b>:
 */
public class NormalConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建链接、链接到RabbitMQ
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置一下链接工厂的链接地址，默认端口是15672
        connectionFactory.setHost("localhost");
        // 创建链接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 在信道中声明交换器
        channel.exchangeDeclare(DirectProducer.EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 申明一个队列
        String queueName = "queue-king";
        channel.queueDeclare(queueName, false, false, false, null);

        // 绑定：将队列(queue-king)与交换器通过 路由键 绑定(king)
        String routeKey = "king";
        channel.queueBind(queueName, DirectProducer.EXCHANGE_NAME, routeKey);
        System.out.println("Waiting for message.....");

        // 申明一个消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received[" + envelope.getRoutingKey() + "]: " + message);
            }
        };

        // 消息正式开始在指定的队列里消费
        channel.basicConsume(queueName, true, consumer);
    }
}
