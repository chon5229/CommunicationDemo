package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * 读写的线程
 */

public class ChatSocket extends Thread {

    private Socket socket;

    public ChatSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * 输出到每个客户端(注意，当前客户端接受不到，毕竟过滤了的嘛)
     *
     * @param out
     */
    public void writeOther(String out) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write(out + "\n");
            //强制输出
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        //读的操作
        //实例化一个输入流
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                //发送
                ChatManger.getChatManger().pulshMessger(this, line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }

    }
}
