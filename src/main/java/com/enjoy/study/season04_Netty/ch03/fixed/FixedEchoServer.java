package com.enjoy.study.season04_Netty.ch03.fixed;

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
import io.netty.handler.codec.FixedLengthFrameDecoder;

import java.net.InetSocketAddress;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 15:18<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 使用 netty 实现的服务端<br/>
 * <b>Description</b>:
 */
public class FixedEchoServer {

    public static final String RESPONSE = "Welcome to Netty!";

    private final int port;

    public FixedEchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final FixedServerHandler handler = new FixedServerHandler();
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
            // 增加固定长度进行分割，长度为客户端发送的信息长度
            ch.pipeline().addLast(new FixedLengthFrameDecoder(FixedEchoClient.FIXED_LENGTH.length()));
            ch.pipeline().addLast(new FixedServerHandler());
        }
    }


    public static void main(String[] args) throws InterruptedException {
        FixedEchoServer server = new FixedEchoServer(10086);
        System.out.println("server is now starting.....");
        server.start();
        System.out.println("server is closed..........");
    }
}
