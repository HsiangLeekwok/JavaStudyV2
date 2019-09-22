package com.enjoy.study.season08_MQ.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/22 18:39<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: 未定义路由键的消费者<br/>
 * <b>Description</b>:
 */
public class FanoutConsumerUnknown {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(FanoutProducer.EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // 声明一个随机队列
        String queueName = channel.queueDeclare().getQueue();

        // 将队列通过路由键绑定到交换器上
        channel.queueBind(queueName, FanoutProducer.EXCHANGE_NAME, "test");
        System.out.println("[" + queueName + "] Waiting for message.....");

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
