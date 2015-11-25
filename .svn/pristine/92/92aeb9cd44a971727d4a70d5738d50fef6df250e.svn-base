package com.syntun.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * 爬虫计数器，统计采集效率
 * 
 * @author mark
 * 
 */
public class CrawlerEnumerator {
	public static String[] emails;
	public static String ip = "";
	public static HashMap<String, Integer> dataMap = new HashMap<String, Integer>();
	public static Integer[] hours = new Integer[24];
	private static int clearDay = 0;
	private static String emailTxt = "email.txt";
	static {
		// 生成发送邮件的小时数
		for (int i = 0; i < 24; i++) {
			hours[i] = i;
		}
		// 获取收件人
		try {
			ArrayList<String> al = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(emailTxt));
			String data = br.readLine();
			while (data != null) {
				if (!data.trim().equals("")) {
					al.add(data.trim());
				}
				data = br.readLine();
			}
			emails = new String[al.size()];
			al.toArray(emails);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 启动计数器
	 */
	public static void startEnumerator() {
		// 获取IP
		ip = getLocalIp();
		// 启动定时发送统计邮件
		new Thread(new SendEnumeratorMail()).start();
	}

	/**
	 * 增加解析次数
	 */
	public synchronized static void addEnumerator() {
		DateTimeFormatter dft = DateTimeFormat.forPattern("HH:mm");
		DateTime dt = new DateTime();
		int day = dt.getDayOfMonth();
		int hour = dt.getHourOfDay();
		int minute = dt.getMinuteOfHour();
		if (hour == 0 && minute == 0 && clearDay != day) {
			dataMap.clear();
			clearDay = day;
		}
		String timeStr = dt.toDateTime().toString(dft);
		if (dataMap.containsKey(timeStr)) {
			dataMap.put(timeStr, dataMap.get(timeStr) + 1);
		} else {
			dataMap.put(timeStr, 1);
		}
	}

	/**
	 * 获取本地IP
	 * 
	 * @return
	 */
	private static String getLocalIp() {
		String localIp = "";
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			boolean isfined = false;
			while (netInterfaces.hasMoreElements() && !isfined) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ei = ni.getInetAddresses();
				while (ei.hasMoreElements()) {
					InetAddress ia = ei.nextElement();
					String[] localhostIpArr = ia.getHostAddress().split("\\.");
					if (localhostIpArr.length == 4
							&& (localhostIpArr[0] + localhostIpArr[1])
									.equals("192168")) {
						localIp = ia.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("获取本地ip失败");
		}
		return localIp;

	}

	public static void main(String[] args) {
		System.out.println("abc");
	}
}

/**
 * 定时发送邮件
 * 
 * @author mark
 * 
 */
class SendEnumeratorMail implements Runnable {
	@Override
	public void run() {
		while (true) {
			System.out.println("进入发邮件系统~~~~~~~~~~~~~");
			DateTime dt = new DateTime();
			int minute = dt.getMinuteOfHour();
			int hour = dt.getHourOfDay();
			System.out.println(hour + minute + ":" + hour + ":" + minute);
			System.out.println(Arrays.asList(CrawlerEnumerator.hours).contains(
					hour));
			System.out.println(CrawlerEnumerator.hours.toString());
			if (Arrays.asList(CrawlerEnumerator.hours).contains(hour)&& minute == 20) {
				System.out.println("开始发邮件~~~~~~~~~~~~~");
				String cnt = "统计结果：<br />";
				HashMap<String, Integer> map = CrawlerEnumerator.dataMap;
				// 排序
				List<Map.Entry<String, Integer>> infoIds =
				        new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
				Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {   
				    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {      
				        //return (o2.getValue() - o1.getValue()); 
				        return Integer.parseInt(o2.getKey().replace(":", "")) - Integer.parseInt(o1.getKey().replace(":", ""));
				    }
				});
				for (String time : map.keySet()) {
					cnt += time + "---" + map.get(time) + "<br />";
				}
				try {
					SyntunEmail.sendSimpleEmail(CrawlerEnumerator.emails,
							CrawlerEnumerator.ip + "采集效率统计", cnt, null, "html");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			SleepUtil.sleep(1000 * 50);
		}
	}
}
