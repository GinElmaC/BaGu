package com.GinElmaC.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
//    public static void main(String[] args) throws IOException {
//        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//        /**
//         * 配置所有的读取都是非阻塞的，也就是read方法不阻塞
//         */
//        serverSocketChannel.configureBlocking(false);
//        /**
//         * 绑定端口
//         */
//        serverSocketChannel.bind(new InetSocketAddress("localhost",8080));
//        /**
//         * 注册Selector
//         */
//        Selector selector = Selector.open();
//        //注册selector，这个selector监听accept事件
//        //事实上我们这里只有SelectionKey.OP_ACCEPT的accept事件，因为我们的ServerSocketChannel大马路是没有读写能力的，只有SocketChannel小马路有读写能力
//        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//
//        while(true){
//            //selector会阻塞，直到监听到了accept事件
//            selector.select();
//            //获取监听到的所有selectedKey，也就是监听到的channel，当channel连接到服务器后，每一个channel就会对应一个唯一的key
//            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//            while(iterator.hasNext()){
//                //遍历迭代器获取Key，每一个Key中会有一个channel
//                SelectionKey next = iterator.next();
//                //因为Selector不知道是否处理结束了，所以这里需要手动remove掉
//                iterator.remove();
//                //如果这个key是连接事件
//                if(next.isAcceptable()){
//                    //获取channel
//                    SelectableChannel channel = next.channel();
//                    ServerSocketChannel serverSocketChannel1 = (ServerSocketChannel) channel;
//                    //获取客户端channel
//                    SocketChannel accept = serverSocketChannel1.accept();
//                    //设置非阻塞
//                    accept.configureBlocking(false);
//                    //监听事件，监听这个channel什么时候是可读的
//                    accept.register(selector,SelectionKey.OP_READ);
//                    System.out.println("客户端连接了"+accept.getRemoteAddress());
//                }
//                //如果这个key是可读事件
//                if(next.isReadable()){
//                    SocketChannel channel = (SocketChannel) next.channel();
//                    //其实这个ByteBuffer就是对于byte数组的封装
//                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//                    //这个方法由于我们配置过，所以是非阻塞的
//                    int length = channel.read(byteBuffer);
//                    if(length == -1){
//                        System.out.println("客户端断开连接"+channel.getRemoteAddress());
//                        //代表连接已经断开了
//                        channel.close();
//                    }else{
//                        //我们的Buffer有两个状态，一个是“读取状态”，一个是“写入状态”，我们读取完后会自动变为“写入状态”，这里转换为读取
//                        byteBuffer.flip();
//                        //获取buffer中还剩的字节数据
//                        byte[] buffer = new byte[byteBuffer.remaining()];
//                        byteBuffer.get(buffer);
//                        String msg = new String(buffer);
//                        System.out.println(msg);
//                    }
//
//                }
//            }
//        }
//    }
    public static void main(String[] args) throws IOException {
        //开启服务器
        ServerSocketChannel sscl = ServerSocketChannel.open();
        //设置非阻塞
        sscl.configureBlocking(false);
        //绑定端口
        sscl.bind(new InetSocketAddress("localhost",8080));
        //开启Selector
        Selector selector = Selector.open();
        //注册Selector
        sscl.register(selector,SelectionKey.OP_ACCEPT);

        while(true){
            //阻塞获取事件
            selector.select();
            //能走到这一步，代表selector已经读取到了事件，这个事件可能是多个
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                //如果事件是连接事件，则先取出用户的大马路，然后获取小马路，然后注册“写事件”
                if(next.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    SocketChannel client = channel.accept();
                    client.register(selector,SelectionKey.OP_READ);
                }else if(next.isReadable()){
                    //如果是可读事件
                    SocketChannel client = (SocketChannel) next.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    client.read(byteBuffer);
                }
            }
        }
    }
}
