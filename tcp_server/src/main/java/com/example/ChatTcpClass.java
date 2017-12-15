package com.example;

public class ChatTcpClass {
    /**
     * 主程序(维护了一个聊天室)
     *
     * @param args
     */
    public static void main(String[] args) {
        //开启一个线程
        new ChatTcpThred().start();
    }
}
