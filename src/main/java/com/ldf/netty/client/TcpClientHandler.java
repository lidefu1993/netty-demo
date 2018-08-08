package com.ldf.netty.client;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by ldf on 2018/8/8.
 */
public class TcpClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TcpClientHandler.class);

    /**
     * 客户端与服务端连接后调用此方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] req = " CLIENT ---> REQUEST".getBytes();
        ByteBuf msg = Unpooled.buffer(req.length);
        msg.writeBytes(req);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String serverResponser = new String(req, "UTF-8");
        System.out.println("FROM SERVER IS : " + serverResponser);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("EXCEPTION : ", cause);
        ctx.close();
    }

}
