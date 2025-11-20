package com.GinElmaC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;

public class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("localhost",8080));
        try {
            while(true){
                Socket client = serverSocket.accept();
                InputStream inputStream = client.getInputStream();
                byte[] input = new byte[1024];
                String str = "";
                int read = 0;
                while((read = inputStream.read(input)) != -1){
                    str+=(new String(input));
                }
                System.out.println(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket.close();
        }
    }
}
