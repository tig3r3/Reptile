package com.syntun.tools;

import java.io.*;
import java.net.*;

public class GrabRateClient {
	private static String ip = "";
	private GrabAvgData gad;
	
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		GrabRateClient.ip = ip;
	}
	public GrabRateClient() throws ClassNotFoundException {
		if(ip.isEmpty())
			return;
		try {
			Socket s = new Socket();
			s.connect(new InetSocketAddress(GrabRateClient.ip, 5000),1000);
			InputStream inStream = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(inStream);
			
			gad = (GrabAvgData) ois.readObject();
			ois.close();
			s.close();
		} catch (UnknownHostException e) {
			System.out.println("nothing");
		} catch (IOException e) {
			System.out.println("nothing too");
		}
	}
	public static GrabAvgData getObj() throws ClassNotFoundException {
		GrabRateClient grc = new GrabRateClient();
		return grc.gad;
	}
	public static void main(String[] args) throws ClassNotFoundException {
		
	}
}
