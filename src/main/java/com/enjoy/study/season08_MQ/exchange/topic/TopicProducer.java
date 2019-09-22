package com.enjoy.study.season08_MQ.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/22 19:19<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Topic方式消息生产者<br/>
 * <b>Description</b>:
 */
public class TopicProducer {

    final static String EXCHANGE_NAME = "topic_procedure";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        // todo
        // 声明 topic 方式交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        // 声明路由
        String[] teachers = {"king", "mark", "james"};
        for (int i = 0; i < 3; i++) {
            String[] modules = {"kafka", "jvm", "redis"};
            for (int j = 0; j < 3; j++) {
                String[] servers = {"A", "B", "C"};
                for (int k = 0; k < 3; k++) {
                    // 发送消息
                    String message = "Hello Topic_[" + i + "," + j + "," + k + "]";
                    String routeKey = teachers[i % 3] + "." + modules[j % 3] + "." + servers[k % 3];
                    channel.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes());
                    System.out.println("[x] Sent '" + routeKey + "': '" + message + "'");
                }
            }
        }

        // 关闭信道和连接
        channel.close();
        connection.close();
    }
}
