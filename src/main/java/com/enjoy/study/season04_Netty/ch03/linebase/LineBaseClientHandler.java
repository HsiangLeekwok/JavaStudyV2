package com.enjoy.study.season04_Netty.ch03.linebase;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * <b>Author</b>: Hsiang Leekwok<br/>
 * <b>Date</b>: 2019/07/16 15:27<br/>
 * <b>Version</b>: 1.0<br/>
 * <b>Subject</b>: 客户端入站处理 handler 实现类<br/>
 * <b>Description</b>:
 */
public class LineBaseClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    /**
     * 当客户端读到数据后就会执行
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("client accept data: " + byteBuf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 连接建立以后
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf msg;
        for (int i = 0; i < 100; i++) {
            String request = "Hello Netty " + i + System.getProperty("line.separator");
            msg = Unpooled.buffer(request.length());
            msg.writeBytes(request.getBytes());
            ctx.writeAndFlush(msg);
        }
    }

    /*发生异常时*/
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
