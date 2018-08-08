package com.ldf.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Created by ldf on 2018/8/8.
 */
public class TcpClient {

    public void connect(int port, String ip) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TcpClientHandler());
                    }
                });
        ChannelFuture cf = bootstrap.connect(ip,port).sync();
        cf.channel().closeFuture().sync();
    }

    public static void main(String[] args) throws InterruptedException {
        new TcpClient().connect( 20000,"172.16.74.76");
    }

}
