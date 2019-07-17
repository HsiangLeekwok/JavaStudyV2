package com.enjoy.study.season04_Netty.ch03.linebase;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.net.InetSocketAddress;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 15:18<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 基于 Netty 实现的客户端<br/>
 * <b>Description</b>:
 */
public class LineBaseEchoClient {

    private final int port;
    private final String host;

    public LineBaseEchoClient(int port, String host) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        // 事件 loop 组，相当于线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 客户端启动必备，启动器
            Bootstrap bootstrap = new Bootstrap();
            // 传入group
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)// 指定使用NIO进行网路传输
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelHandleInitializerImpl());// 增加时间处理器
            ChannelFuture sync = bootstrap.connect().sync();// 阻塞方式连接远程
            // 阻塞直到关闭连接
            sync.channel().closeFuture().sync();
        } finally {
            // 同步优雅关闭 eventLoop
            group.shutdownGracefully();
        }
    }

    private class ChannelHandleInitializerImpl extends ChannelInitializer<Channel> {
        @Override
        protected void initChannel(Channel ch) throws Exception {
            // 增加回车换行符进行分割
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new LineBaseClientHandler());
        }
    }


    public static void main(String[] args) throws Exception {
        new LineBaseEchoClient(10086, "127.0.0.1").start();
    }
}
