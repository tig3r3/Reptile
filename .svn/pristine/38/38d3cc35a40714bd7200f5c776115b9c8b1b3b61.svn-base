package com.syntun.replace;


import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DATEDreplace implements replaceParameter{

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		String[] pArr = exeParam.split("\\+");
		String start = pArr[0];
		String end = pArr[1];
		String type = pArr[2];
		System.out.println(type);
//		LinkedList<ParameInfo> linkedList=new LinkedList<ParameInfo>();
//		linkedList=getInterDays(start, end);
//		LinkedList<ParameInfo> linkedList2=new LinkedList<ParameInfo>();
//		linkedList2.add(e);
		return getInterDays(start, end,type);
	}
	
	public static LinkedList<ParameInfo> getInterDays(String startDay,
			String endDay,String type) {
		Pattern pt = Pattern.compile("[\\d]{4}\\-[\\d]{2}\\-[\\d]{2}");
		Matcher sm = pt.matcher(startDay);
		if (!sm.find()) {
			System.out.println("error:ss");
			System.exit(0);
		}
		Matcher em = pt.matcher(endDay);
		if (!em.find()) {
			System.out.println("error:ss");
			System.exit(0);
		}
		LinkedList<ParameInfo> result = new LinkedList<ParameInfo>();
		if (startDay.equals(endDay)) {
			result.add(new ParameInfo(startDay + "", startDay + ""));
		} else {
			DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime startDate = DateTime.parse(startDay);
			DateTime endDate = DateTime.parse(endDay);
			Days InterDays = Days.daysBetween(startDate, endDate);
			int days = InterDays.getDays();
			int ddday=1;
			if(type.split(":")[0].equals("dd")){
				ddday=Integer.parseInt(type.split(":")[1]);
			}
			for (int i = 0; i <= days; i++) {
				if(i%ddday==0){
					result.add(new ParameInfo(startDate.plusDays(i).toString(dtf)
							+ "", startDate.plusDays(i).toString(dtf) + ""));
				}
			
			}
		}
		return result;
	}

	public static void main(String[] args) {
		DATEDreplace d = new DATEDreplace();
		for (ParameInfo p : d.execParame("2014-11-12+2014-12-12+dd:7")) {
			System.out.println(p.getValue());
		}
	}
}
