package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

/**
 *
 */

public class ChatTcpThred extends Thread {


    @Override
    public void run() {
        try {
            //实例化，添加一个端口号（0-65535）,注意端口被占用的情况
            ServerSocket serverSocket = new ServerSocket(12345);
            //一个死循环
            while (true) {
                //接受连接
                Socket socket = serverSocket.accept();
                //弹出提示
                JOptionPane.showMessageDialog(null, "有客户端连接到本机的12345啦");
                //开启一个线程（每一个连接都是一个独立的线程）
                ChatSocket chatSocket = new ChatSocket(socket);
                chatSocket.start();
                ChatManger.getChatManger().addChatSocket(chatSocket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
