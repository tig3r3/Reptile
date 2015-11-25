package com.syntun.tools;

import java.io.*;
import java.net.*;
import java.util.*;

public class GrabAvgOutServer {
	public static final String bm="GBK"; 
	public static void main(String[] args) {
		try {
			int i = 1;
			GrabAvgOutServer gaos = new GrabAvgOutServer();
			ServerSocket s = new ServerSocket(3000);
			System.out.println("外部服务器已启动");
			while(true) {
				Socket incoming = s.accept();
				Runnable r = gaos.new ThreadGrabHandler(incoming);
				Thread t = new Thread(r);
				t.start();
				System.out.println("进程：" + i);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class ThreadGrabHandler implements Runnable {
		private Socket incoming;
		public ThreadGrabHandler(Socket i) {
			incoming = i;
		}
		@Override
		public void run() {
			try {
				System.out.println("已接收一个客户端请求");
				GrabAvgPub gap = new GrabAvgPub();
				try{
					OutputStream outStream = incoming.getOutputStream();
					BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outStream, bm));
					
					InputStream inStream = incoming.getInputStream();
					Scanner in = new Scanner(inStream);
					
					while(in.hasNext()) {
						String line = in.nextLine().trim();
						System.out.println("接受的字符串：" + line);
						
						//全部服务器状态
						if(line.equals("a")) {
							bw.write(gap.getAllServer());
							bw.flush();
							bw.close();
							
							System.out.println("全部服务器输出关闭！");
							break;
						}
						//服务器列表
						if(line.equals("s")) {
							bw.write(gap.getServerList());
							bw.flush();
							bw.close();
							
							System.out.println("服务器列表输出关闭！");
							break;
						}
						//单个服务器详情
						if(line.matches("\\d{1,3}[\\-\\d\\:]{0,}")) {
							String ip = "";
							String time = "";
							if(line.indexOf("-") != -1) {
								String[] lineRs =line.split("-");
								ip = lineRs[0];
								time = lineRs[1];
							} else {
								ip = line;
							}
							System.out.println("请求IP：192.168.0." + ip + ";上次请求时间：" + time);
							bw.write(gap.getOneServer("192.168.0." + ip, time));
							bw.flush();
							bw.close();
							System.out.println("单服务器输出关闭！");
							break;
						}
						//服务器控制（重启、清库）
						if(line.matches("\\d{1,3}\\-(restart|clearrestart)")) {
							System.out.println("进入服务器控制！~");
							String ip = "";
							String cmd = "";
							if(line.indexOf("-") != -1) {
								String[] lineRs =line.split("-");
								ip = lineRs[0];
								cmd = lineRs[1];
							}
							System.out.println("请求IP：192.168.0." + ip + ";请求命令：" + cmd);
							bw.close();
							System.out.println("服务器控制输出关闭！");
							break;
						}
					}
					System.out.println("输出完成！");
					in.close();
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
}
