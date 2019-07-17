package com.enjoy.study.season04_Netty.ch03.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 15:18<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 使用 netty 实现的服务端<br/>
 * <b>Description</b>:
 */
public class DelimiterEchoServer {

    public static final String DELIMITER="#~";

    private final int port;

    public DelimiterEchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final DelimiterServerHandler handler = new DelimiterServerHandler();
        // 指定 NIO 方式
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(group)// 传入reactor
                    .channel(NioServerSocketChannel.class)// 指定服务端数据处理模型
                    .localAddress(new InetSocketAddress(port))// 绑定端口
                    // 服务端需要指定childHandler来处理客户端的数据通讯
                    // 如果用handler()方法则只是在ServerSOcketChannel上进行操作
                    .childHandler(new ChannelInitializerImpl());
            ChannelFuture sync = bootstrap.bind().sync();
            sync.channel().closeFuture().sync();
        } finally {
            // 同步阻塞优雅关闭
            group.shutdownGracefully().sync();
        }
    }

    private class ChannelInitializerImpl extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            // 增加自定义符号进行分割
            ByteBuf delimiter = Unpooled.copiedBuffer(DelimiterEchoServer.DELIMITER.getBytes());
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
            ch.pipeline().addLast(new DelimiterServerHandler());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        DelimiterEchoServer server = new DelimiterEchoServer(10086);
        System.out.println("server is now starting.....");
        server.start();
        System.out.println("server is closed..........");
    }
}
