package com.GinElmaC.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer {
    public static void main(String[] args) {
        /**
         * 服务器的Bootstrap
         */
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //boss线程组
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //work线程组
        NioEventLoopGroup work = new NioEventLoopGroup();
        /**
         * 设置线程组
         */
        serverBootstrap.group(boss,work);
        /**
         * 设置channel
         */
        serverBootstrap.channel(NioServerSocketChannel.class);
        /**
         * 对于每一个子连接设置什么handler，pipeline每个线程都有自己独立的，这里我们一般填写一个channel初始化handler
         * new ChannelInitializer有一个泛型，表示你要对哪一个类进行初始化，我们肯定是对SocketChannel进行初始化
         */
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            //对channel进行配置
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                /**
                 * 向pipeline添加处理器
                 */
                socketChannel.pipeline()
                        //Netty帮我们实现的处理器，可以自动根据发送的消息的"\n"或者"\r\n"来解决粘包问题
                        .addLast(new LineBasedFrameDecoder(1024))
                        //添加解码器，将客户端传进来的byte数组解码为String
                        .addLast(new StringDecoder())
                        //添加处理String类型消息的处理器
                        .addLast(new SimpleChannelInboundHandler<String>() {
                            //这个函数真正读取了我们的消息，也就是msg，这个消息是由前面的handler解码得到的
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {

                                System.out.println(msg);
                            }
                        });
            }
        });
        /**
         * 绑定端口，返回值ChannelFuture是一个非常重要的东西，代表服务器是否成功开启
         * 因为netty中几乎所有操作都是一个异步的，通过这个Future类型的返回值我们可以获取执行结果信息
         */
        ChannelFuture bindFuture = serverBootstrap.bind(8080);
        bindFuture.addListener(f->{
            if(f.isSuccess()){
                System.out.println("服务器成功监听8080");
            }else{
                System.out.println("服务器监听端口失败");
            }
        });
    }
}
