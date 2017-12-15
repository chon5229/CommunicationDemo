package com.example;

public class ChatTcpClass {
    /**
     * 主程序
     *
     * @param args
     */
    public void main(String[] args) {
        //开启一个线程
        new ChatTcpThred().start();
    }
}
