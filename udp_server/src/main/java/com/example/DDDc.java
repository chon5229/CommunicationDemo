package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by HP on 2017/12/16.
 */

public class DDDc {
    public static void main(String[] args) {
        MulticastSocket mSocket = null;//生成套接字并绑定30001端口
        try {
            mSocket = new MulticastSocket(30001);
            InetAddress group = InetAddress.getByName("239.0.0.1");//设定多播IP
            mSocket.joinGroup(group);//接受者加入多播组，需要和发送者在同一组
            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);//创建接收报文，以接收通过多播传递过来的报文
                mSocket.receive(packet);//接收多播报文，程序停滞等待直到接收到报文
                System.out.println(packet.getAddress().getHostAddress() + "说:" + new String(buf, 0, packet.getLength()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert mSocket != null;
            mSocket.close();//关闭套接字
        }

    }
}
