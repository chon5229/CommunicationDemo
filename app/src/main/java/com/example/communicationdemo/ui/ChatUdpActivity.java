package com.example.communicationdemo.ui;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.communicationdemo.R;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 基于udp的单播实现
 * 本项目所有ip地址根据情况改成自己的
 */
public class ChatUdpActivity extends AppCompatActivity {
    final static int udpPort = 12345;
    final static String hostIp = "192.168.1.106";
    private static DatagramSocket socket = null;
    private static DatagramPacket packetSend, packetRcv;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[1024]; //接收消息
    private EditText edt;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_udp);
        initView();
        new Thread(new UdpHelper()).start();
    }

    private void initView() {
        edt = (EditText) findViewById(R.id.edt);
        tv = (TextView) findViewById(R.id.tv);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String s = (String) msg.obj;
                    tv.append(s + "\n");
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        udpLife = false;
    }

    /**
     * 输入拜拜及断开连接
     *
     * @param view
     */
    public void send(View view) {
        final String trim = edt.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendMsg(trim);
            }
        }).start();
        if (trim.equals("拜拜")) {
            tv.append("服务器连接已断开");
        }

    }

    /**
     * 发送数据到服务器（UDP  自己构建的java服务器，udp_server 里面的chatUdpClass）
     * 不管咋样你的先运行起自己的服务器吧。。。
     *
     * @param msg
     */
    public void sendMsg(String msg) {
        InetAddress hostAddress = null;

        try {
            hostAddress = InetAddress.getByName(hostIp);
        } catch (UnknownHostException e) {
            Log.i("====", "未找到服务器");
            e.printStackTrace();
        }

        packetSend = new DatagramPacket(msg.getBytes(), msg.getBytes().length, hostAddress, udpPort);

        try {
            socket.send(packetSend);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("====", "发送失败");
        }
        //   socket.close();
    }

    private class UdpHelper implements Runnable {

        @Override
        public void run() {

            try {
                socket = new DatagramSocket();
                socket.setSoTimeout(3000);//设置超时为3s
            } catch (SocketException e) {
                Log.e("====", "建立接收数据报失败");
                e.printStackTrace();
            }
            packetRcv = new DatagramPacket(msgRcv, msgRcv.length);
            while (udpLife) {
                try {
                    Log.e("====", "UDP监听");
                    socket.receive(packetRcv);
                    String RcvMsg = new String(packetRcv.getData(), packetRcv.getOffset(), packetRcv.getLength());
                    //将收到的消息发给主界面
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = RcvMsg;
                    handler.sendMessage(msg);
                    Log.e("====", RcvMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Log.e("====", "UDP监听关闭");
            if (socket != null) {
                socket.disconnect();//断开套接字
            }
            if (socket != null) {
                socket.close();//关闭套接字
            }
        }
    }
}
