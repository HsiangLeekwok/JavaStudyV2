package com.enjoy.study.season08_MQ.exchange.topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/22 19:27<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 关注全部路由 #<br/>
 * <b>Description</b>:
 */
public class King_All_AConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        // todo
        // 声明 topic 方式交换器
        channel.exchangeDeclare(TopicProducer.EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 声明一个随机队列
        String queueName=channel.queueDeclare().getQueue();

        // todo
        // 关注的路由
        channel.queueBind(queueName,TopicProducer.EXCHANGE_NAME,"king.*.A");
        System.out.println("[*] Waiting for message.....");

        // 创建消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received '" + envelope.getRoutingKey() + "': " + message);
            }
        };

        // 在队列上消费消息
        channel.basicConsume(queueName, true, consumer);
    }
}
