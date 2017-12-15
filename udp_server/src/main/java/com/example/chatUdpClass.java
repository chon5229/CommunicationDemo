package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class chatUdpClass {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            //建立哪个UDP接受器
            socket = new DatagramSocket(12345);
            //建立哪个集装箱
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            //装东西
            socket.receive(packet);   //这个饭刚发是一个阻塞的方法,意思是这个方法一定要等到有数据的到来否则一致等待
            System.out.println(packet.getAddress().getHostAddress() + "说:" + new String(buf, 0, packet.getLength()));
            //打印数据

            String data = getData();   //发送的数据
            socket = new DatagramSocket();   //整个码头
            DatagramPacket packets = new DatagramPacket(data.getBytes(), data.getBytes().length, InetAddress.getLocalHost(), 9000);  //打包
            socket.send(packets);


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    /**
     * 获取控制台的那个数据
     *
     * @return
     * @throws IOException
     */
    private static String getData() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        line = reader.readLine();
        return line;
    }
}
