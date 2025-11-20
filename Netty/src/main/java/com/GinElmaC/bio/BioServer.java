package com.GinElmaC.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class BioServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        while (true) {
            Socket socket = serverSocket.accept();
            int length;
            byte[] input = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while((length = inputStream.read(input)) != -1){
                String msg = new String(input,0,length);
                System.out.println(msg);
            }
            System.out.println("客户端断开");
        }
    }
}
