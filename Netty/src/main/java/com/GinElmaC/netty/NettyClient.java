package com.GinElmaC.netty;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClient {
    public static void main(String[] args) {
        //客户端BootStrap
        Bootstrap bootstrap = new Bootstrap();
        //设置客户端线程组
        bootstrap.group(new NioEventLoopGroup());
        //设置客户端channel
        bootstrap.channel(NioSocketChannel.class);
        //客户端独立设置handler
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        //我们的需求是发送字符串，所以这里添加将String编码为字符数组的处理器
                        .addLast(new StringEncoder());
            }
        });
        /**
         * 连接，ChannelFuture表示客户端是否成功连接到服务器
         */
        ChannelFuture connect = bootstrap.connect("localhost", 8080);
        connect.addListener(f->{
            if(f.isSuccess()){
                System.out.println("客户端成功连接8080");
                /**
                 * 通过channel发送消息
                 */
                connect.channel().writeAndFlush("hello");
            }else{
                System.out.println("客户端连接8080失败");
            }
        });
    }
}
