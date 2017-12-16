package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by HP on 2017/12/16.
 */

public class DDDs {
    public static void main(String[] args) {
        MulticastSocket mSocket = null;//生成套接字并绑定30001端口
        try {
            mSocket = new MulticastSocket(30001);
            InetAddress group = InetAddress.getByName("239.0.0.1");//设定多播IP
            mSocket.joinGroup(group);//加入多播组，发送方和接受方处于同一组时，接收方可抓取多播报文信息
            mSocket.setTimeToLive(4);//设定TTL
            while (true) {
                String data = getData();
                byte[] buff = data.getBytes("utf-8");//设定多播报文的数据
//设定UDP报文（内容，内容长度，多播组，端口）
                DatagramPacket packet = new DatagramPacket(buff, buff.length, group, 30001);
                mSocket.send(packet);//发送报文
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert mSocket != null;
            mSocket.close();//关闭套接字
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
