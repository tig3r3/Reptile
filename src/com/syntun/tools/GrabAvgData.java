package com.syntun.tools;

import java.io.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.TimeZone;

public class GrabAvgData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	/* 保存多少分钟记录 */
	private int keepMinuter = 100;
	/* 上次的小时 */
	private int lastHour = -1;

	/* 上次的分钟 */
	private int lastMinuter = -1;

	/* 存储每分钟的解析数量 */
	private LinkedList<Integer> numList = new LinkedList<Integer>();

	/* 存储numList对应的时间 */
	private LinkedList<String> timeList = new LinkedList<String>();

	private static GrabAvgData gad = new GrabAvgData();

	private static Calendar c = Calendar.getInstance();

	private GrabAvgData() {

	}

	public static GrabAvgData getGrabAvgData() {
		return gad;
	}

	static Calendar cTest = Calendar.getInstance();

	public LinkedList<Integer> getNumList() {
		return this.numList;
	}

	public LinkedList<String> getTimeList() {
		return this.timeList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 增加数量
	 */
	public synchronized static void addNum() {
		c.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		c.setTimeInMillis(System.currentTimeMillis());
		int minuter = c.get(Calendar.MINUTE);
		int hour = c.get(Calendar.HOUR_OF_DAY);

		if (gad.lastHour == -1 || gad.lastMinuter == -1 || hour != gad.lastHour
				|| minuter != gad.lastMinuter) {
			gad.numList.add(0);
			gad.timeList.add(hour + ":" + minuter);
			gad.lastHour = hour;
			gad.lastMinuter = minuter;
		}
		int listIndex = gad.numList.size() - 1;
		gad.numList.set(listIndex, (gad.numList.get(listIndex) + 1));
		if (listIndex >= gad.keepMinuter) {
			gad.numList.removeFirst();
			gad.timeList.removeFirst();
		}
	}

}
