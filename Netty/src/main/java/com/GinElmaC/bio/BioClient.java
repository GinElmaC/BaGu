package com.GinElmaC.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BioClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread tom = new Thread(() -> {
            try {
                sendHello();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"tom");
        Thread joy = new Thread(() -> {
            try {
                sendHello();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },"joy");

        tom.start();
        joy.start();
        tom.join();
        joy.join();
    }

    public static void sendHello() throws IOException, InterruptedException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost",8080));
        OutputStream outputStream = socket.getOutputStream();
        for(int i = 0;i<10;i++){
            outputStream.write(("hello"+i+Thread.currentThread().getName()).getBytes());
            outputStream.flush();
        }
        Thread.sleep(1000);
        socket.close();
    }
}
