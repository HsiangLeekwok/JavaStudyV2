package com.enjoy.study.season04_Netty.ch04.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/07 20:31<br/>
 * <b>Version</b>: v1.0<br/>
 * <b>Subject</b>: Netty 服务端处理器 <br/>
 * <b>Description</b>:
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    // 数据完整读取之后会调用(即使分包过来的数据，也只会调用 1 次)
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in= (ByteBuf) msg;
        System.out.println("Server accept "+ in.toString());
        ctx.write(in);
        //super.channelRead(ctx, msg);
    }

    // 缓冲区每读完一次，调用一次
    // 如果数据1次就读取完毕，则会先调用read，然后netty会一直再继续读缓冲区，直至达到缓冲区大小之后才会调用complete方法
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
