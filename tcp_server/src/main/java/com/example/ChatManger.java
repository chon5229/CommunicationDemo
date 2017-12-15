package com.example;

import java.util.Vector;

/**
 * 一个管理的单例
 */

public class ChatManger {
    private ChatManger() {
    }

    private static ChatManger chatManger = null;

    public static ChatManger getChatManger() {
        if (chatManger == null)
            chatManger = new ChatManger();
        return chatManger;
    }

    //一个管理线程的集合
    private Vector<ChatSocket> chatSockets = new Vector<>();

    //每有一个连接的时候添加
    public void addChatSocket(ChatSocket socket) {
        chatSockets.add(socket);
    }

    //向除了当前的ChatSocket之外的所有ChatSocket发送消息
    public void pulshMessger(ChatSocket socket, String out) {
        //基本的，遍历
        for (int i = 0; i < chatSockets.size(); i++) {
            //获取实例
            ChatSocket chatSocket = chatSockets.get(i);
            //不是本连接的时候发送
            if (!socket.equals(chatSocket)) {
                //发送
                chatSocket.writeOther(out);
            }
        }
    }
}
