package com.syntun.tools;

import java.io.*;
import java.net.*;
import java.util.Calendar;

public class GrabRateServer implements Runnable{	
	
	public static void main(String[] args) {
		boolean doit = true;
		while(doit) {
			GrabAvgData.addNum();
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR);
			int minuter = c.get(Calendar.MINUTE);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(minuter == 30 && hour == 4) doit = false;
		}
		System.out.println("数据生成完成！");
		try {
			ServerSocket s = new ServerSocket(5000);
			while(true) {
				Socket incoming = s.accept();
				Runnable r = new GrabRateServer().new ThreadGrabHandler(incoming);
				Thread t = new Thread(r);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 启动监听服务
	 */
	public static void startSocket() {
		new Thread(new GrabRateServer()).start();
	}
	
	class ThreadGrabHandler implements Runnable {
		private Socket incoming;
		public ThreadGrabHandler(Socket i) {
			incoming = i;
		}
		@Override
		public void run() {
			try {
				GrabAvgData cat = GrabAvgData.getGrabAvgData();
//				System.out.println("打印获得的对象：" + cat.toString());
//				System.out.println("打印获得的所有数量值：" + cat.getNumList().toString());
//				System.out.println("打印获得的所有时间值：" + cat.getTimeList().toString());
//				System.out.println("第一个数量值是：" + cat.getNumList().getFirst());
//				System.out.println("第一个时间值是：" + cat.getTimeList().getFirst());
				try{
					OutputStream outStream = incoming.getOutputStream();
					ObjectOutputStream oos = new ObjectOutputStream(outStream);
					oos.writeObject(cat);
					oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					incoming.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ServerSocket s = new ServerSocket(5000);
			while(true) {
				Socket incoming = s.accept();
				System.out.println("进来一个socket请求");
				Runnable r = new ThreadGrabHandler(incoming);
				Thread t = new Thread(r);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
