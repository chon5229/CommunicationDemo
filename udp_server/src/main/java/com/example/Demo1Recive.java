package com.example;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * UDP的接受函数
 * @author Administrator
 *
 */
public class Demo1Recive {
	public static void main(String[] args) {
		DatagramSocket socket=null;
		try {
			//建立哪个UDP接受器
			socket=new DatagramSocket(9000);
			//建立哪个集装箱
			byte[] buf=new byte[1024];
			DatagramPacket packet=new DatagramPacket(buf,buf.length);
			//装东西
			socket.receive(packet);   //这个饭刚发是一个阻塞的方法,意思是这个方法一定要等到有数据的到来否则一致等待
			System.out.println(packet.getAddress().getHostAddress()+"说:"+new String(buf,0,packet.getLength()));
			//打印数据
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			socket.close();
		}
	}
}
