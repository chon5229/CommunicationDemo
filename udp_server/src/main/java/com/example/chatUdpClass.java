package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * 单播（这个就是一个模拟，手机发送个数据，这里获取导数据，再把其返回回去）
 * 如果要做成一对一同学，设置ip地址就可以了（不是在这做，方法类似）
 */
public class chatUdpClass {

    public static void main(String[] args) {
        new MyThred().start();
    }


    private static class MyThred extends Thread {
        @Override
        public void run() {
            DatagramSocket socket = null;
            boolean isEnd = false;
            try {
                socket = new DatagramSocket(12345);
                while (!isEnd) {
                    byte[] buf = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);   //这个饭刚发是一个阻塞的方法,意思是这个方法一定要等到有数据的到来否则一致等待
                    String str = new String(buf, 0, packet.getLength());
                    System.out.println(packet.getAddress().getHostAddress() + "说:" + str);
                    //打印数据
                    if (str.equals("拜拜")) {
                        isEnd = true;
                    }
                    String data = "这是服务器返回的数据啊：" + str;   //发送的数据
                    //  socket = new DatagramSocket();
                    //这里是发送数据过来的ip地址
                    InetAddress address = packet.getAddress();
                    //这里是句发送过来的端口号
                    int port = packet.getPort();
                    DatagramPacket packets = new DatagramPacket(data.getBytes(), data.getBytes().length, address, port);  //打包
                    socket.send(packets);
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    socket.disconnect();//断开套接字
                }
                if (socket != null) {
                    socket.close();//关闭套接字
                }
            }
        }
    }
}
