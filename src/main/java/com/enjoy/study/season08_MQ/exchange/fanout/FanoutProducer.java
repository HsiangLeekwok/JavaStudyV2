package com.enjoy.study.season08_MQ.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/09/22 18:26<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Fanout 交换器，广播交换器<br/>
 * <b>Description</b>:
 */
public class FanoutProducer {

    static final String EXCHANGE_NAME = "fanout";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        // 创建连接
        Connection connection = factory.newConnection();

        // 创建信道
        Channel channel = connection.createChannel();

        // todo
        // 声明信道方式
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        // 日志消息级别
        String[] routeKeys = {"king", "mark", "james"};
        for (int i = 0; i < 3; i++) {
            // 每次发送一条消息
            String routeKey = routeKeys[i % 3];
            String message = "Hello World: " + (i + 1);
            // 发送消息
            channel.basicPublish(EXCHANGE_NAME, routeKey, null, message.getBytes());
            System.out.println("[x] Sent '" + routeKey + "': '" + message + "'");
        }
        // 关闭信道
        channel.close();
        // 关闭连接
        connection.close();
    }
}
