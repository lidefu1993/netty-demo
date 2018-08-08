package com.ldf.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

/**
 * Created by ldf on 2018/8/8.
 */
public class TCPServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TCPServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inBuf = (ByteBuf) msg;
        byte[] inBytes = new byte[inBuf.readableBytes()];
        inBuf.readBytes(inBytes);
        String inStr = new String(inBytes, "UTF-8");
        System.out.println(" MESSAGE FROM CLIENT :" + inStr);
        ByteBuf outBuf = Unpooled.copiedBuffer("RESPONSE TO CLIENT ".getBytes());
        ctx.write(outBuf);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //netty的消息不直接写入channel,它放在缓冲数组,调用flush()才写入
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        logger.error("发生异常:{}", cause);
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("终端连接:{}");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final String sessionId = ctx.channel().id().asLongText();
        logger.debug("终端断开连接:{}");
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                logger.error("服务器主动断开连接:{}");
                ctx.close();
            }
        }
    }
}
