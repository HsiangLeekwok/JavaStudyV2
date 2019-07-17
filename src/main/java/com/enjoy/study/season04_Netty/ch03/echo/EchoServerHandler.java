package com.enjoy.study.season04_Netty.ch03.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 15:40<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 服务端入站处理handler实现<br/>
 * <b>Description</b>:
 */

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private AtomicInteger readCount = new AtomicInteger(0);
    private AtomicInteger readCompleteCount = new AtomicInteger(0);

    /**
     * 读取到完整的数据时触发
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        readCount.getAndIncrement();
        System.out.println("channelRead times: " + readCount.get());
        ByteBuf buffer = (ByteBuf) msg;
        String request = buffer.toString(CharsetUtil.UTF_8);
        System.out.println("Server accept: " + request);
        String response = "Hello " + request + ", welcome to netty world.";
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 读取完缓冲区的数据之后触发 complete 事件
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        readCompleteCount.getAndIncrement();
        System.out.println("channelReadComplete times: " + readCompleteCount.get());
        // 强制把缓冲区的数据刷到对端
        //ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
