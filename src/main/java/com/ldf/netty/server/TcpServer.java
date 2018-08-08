package com.ldf.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by ldf on 2018/8/8.
 */
public class TcpServer  {

    /**
     * 无参构造创建的线程池的大小是CPU核心的两倍；
     * 初始化相应数量的线程放入线程池， 每个线程设置一个阻塞队列作为任务队列(LinkedBlockQueue)
     * @param port 端口号
     */
    public void bind(int port) throws InterruptedException {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boosGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChildChannelHandler())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        System.out.println("SERVER-------> START");
        ChannelFuture channelFuture = bootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new TCPServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new TcpServer().bind(20000);
    }
}
