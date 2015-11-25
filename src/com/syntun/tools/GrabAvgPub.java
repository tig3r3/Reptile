package com.syntun.tools;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class GrabAvgPub {
	private static String  serverListUri = "http://192.168.0.136/inspectData/txt/server.txt";
	public static void main(String[] args) {
		GrabAvgPub gap = new GrabAvgPub();
		System.out.println(gap.getServerList());
		System.exit(0);
		try {
			gap.getAllServer();//获取所有服务器当前状态
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
		try {
			gap.getOneServer("127.0.0.1", "");//获取单台服务器的状态
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取单台服务器数据
	 * @param ip
	 * @param time
	 * @return
	 * @throws ClassNotFoundException
	 */
	public String getOneServer(String ip, String time) throws ClassNotFoundException {
		System.out.println("进入单个服务器请求！ ip:" + ip);
		String s = "";
		GrabRateClient.setIp(ip);
		GrabAvgData gad = GrabRateClient.getObj();
		if(gad == null) {
			return "0=0";
		}
		LinkedList<Integer> numList = gad.getNumList();
		LinkedList<String> timeList = gad.getTimeList();
		int num = numList.size();
		ArrayList<String> sRs = new ArrayList<String>();
		if(num == timeList.size()) {
			if(time.isEmpty()) {
				for(int i=0; i<=(num-1); i++) {
//					if(i != 0) s += ",";
//					s += numList.get(i) + "=" + timeList.get(i);
					sRs.add(numList.get(i) + "=" + timeList.get(i));
				}
			} else {
				String[] requestTimeRs = time.split(":");
				int requestTime = Integer.parseInt(requestTimeRs[0]) * 60 + Integer.parseInt(requestTimeRs[1]);
				for(int i=0; i<=(num-1); i++) {
					String[] thisTimeRs = timeList.get(i).split(":");
					int thisTime = Integer.parseInt(thisTimeRs[0]) * 60 + Integer.parseInt(thisTimeRs[1]);
					if(thisTime >= requestTime) {
//						if(i != 0) s += ",";
//						s += numList.get(i) + "=" + timeList.get(i);
						sRs.add(numList.get(i) + "=" + timeList.get(i));
					}
				}
			}
		} else {
			System.out.println("数据不一致！");
		}
		int j =  0;
		for(String ss:sRs) {
			if(j != 0) 
				s += ",";
			s += ss;
			j++;
		}
		return s;
	}
	/**
	 * 获取所有服务器上一分钟状态
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public String getAllServer() throws Exception {
		//服务器列表
		HashMap<String, String> serverList = createServerList();
		Set<String> aSet = serverList.keySet();
		Iterator<String> aaSet = aSet.iterator();
		int i = 0;
		String dataString = "";
		while(aaSet.hasNext()) {
			String ip = aaSet.next();
//			String des = serverList.get(ip);
			String data = getOneServer(ip, "");
			String lastNum = "0";
			String lastTime = "0:0";
			if(!data.isEmpty() && !data.equals("0=0")) {
				String[] dataList = data.split(",");
				if(dataList.length > 2) {
					String[] numList = dataList[(dataList.length -2)].split("=");
					lastNum = numList[0];
					lastTime = numList[1];
				} else {
					String[] numList = dataList[(dataList.length -1)].split("=");
					lastNum = numList[0];
					lastTime = numList[1];
				}
					
				
			}
			if(i != 0) dataString += ",";
			dataString += ip.replace("192.168.0.", "")  + "-" + lastNum + "-" + lastTime;
			i++;
		}
		return dataString;
	}
	/**
	 * 返回经过处理的服务器列表
	 * @return
	 */
	public String getServerList() {
		HashMap<String, String> serverListRs =  new HashMap<String, String>();
		try {
			serverListRs =  createServerList();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<String> aSet = serverListRs.keySet();
		Iterator<String> aaSet = aSet.iterator();
		int i = 0;
		String dataString = "";
		while(aaSet.hasNext()) {
			String ip = aaSet.next();
			String des = serverListRs.get(ip);
			if(i != 0) dataString += ",";
			dataString += ip.replace("192.168.0.", "") + "-" + des;
			i++;
		}
		return dataString;
	}
	/**
	 * 获取服务器列表
	 * @return
	 * @throws IOException
	 */
	private HashMap<String, String> createServerList() throws IOException {
		HashMap<String, String> serverList = new HashMap<String, String>();
		HttpURLConnection httpConn = (HttpURLConnection) new URL(serverListUri).openConnection();
		httpConn.connect();
		InputStreamReader bis = new InputStreamReader(httpConn.getInputStream(),"GBK");
		int line = -1;
		StringBuffer content = new StringBuffer();
		while((line = bis.read()) != -1) {
			char c = (char)line;
			content.append(c);
		}
		String contentString = content.toString();
		String[] list = contentString.split("\n");
		
		for(String lineString:list) {
			System.out.println("server list:" + lineString);
			String[] ipList = lineString.split(",");
			String des = ipList[1];
			des = des.replace("\r", "");
			des = des.replace("\n", "");
			serverList.put(ipList[0], des);
		}
		return serverList;
	}
}
