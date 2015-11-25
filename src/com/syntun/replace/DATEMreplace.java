package com.syntun.replace;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DATEMreplace implements replaceParameter{

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = new GregorianCalendar();  
		String[] pArr = exeParam.split("\\+");
		String start = pArr[0];
		String end = pArr[1];
		String type = pArr[2];
		if(pArr.length==3){
			return getInterDays(start, end,type,"0");
		}
		if(pArr[3].equals("type:1")){
			calendar.set(Integer.parseInt(pArr[0].split("-")[0]),Integer.parseInt(pArr[0].split("-")[1])-1, 1);  
			start=dft.format(calendar.getTime());
			calendar.set(Integer.parseInt(pArr[1].split("-")[0]),Integer.parseInt(pArr[1].split("-")[1])-1, 1);  
			end=dft.format(calendar.getTime());
		}
		if(pArr[3].equals("type:2")){
			calendar.set(Integer.parseInt(pArr[0].split("-")[0]),Integer.parseInt(pArr[0].split("-")[1]), 0);  
			start=dft.format(calendar.getTime());
			calendar.set(Integer.parseInt(pArr[1].split("-")[0]),Integer.parseInt(pArr[1].split("-")[1]), 0);  
			end=dft.format(calendar.getTime());
		}
//		System.out.println(start);
//		System.out.println(end);
		return getInterDays(start, end,type,pArr[3]);
	}
	
	public static LinkedList<ParameInfo> getInterDays(String startDay,
			String endDay,String type,String ss) {
		Calendar calendar = new GregorianCalendar();  
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
//			Days InterDays = Days.daysBetween(startDate, endDate);
			Months month= Months.monthsBetween(startDate, endDate);
			int months = month.getMonths();
			int MMday=1;
			if(type.split(":")[0].equals("MM")){
				MMday=Integer.parseInt(type.split(":")[1]);
			}
			for (int i = 0; i <= months; i++) {
				if(i%MMday==0){
					result.add(new ParameInfo(startDate.plusMonths(i).toString(dtf)
							+ "", startDate.plusMonths(i).toString(dtf) + ""));
				}
			
			}
		}
		LinkedList<ParameInfo> results = new LinkedList<ParameInfo>();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		for (ParameInfo p :result) {
			String s=p.getValue();
			if(ss.equals("type:1")){
			calendar.set(Integer.parseInt(s.split("-")[0]),Integer.parseInt(s.split("-")[1])-1, 1);
			s=dft.format(calendar.getTime());
			}
			if(ss.equals("type:2")){
				calendar.set(Integer.parseInt(s.split("-")[0]),Integer.parseInt(s.split("-")[1]), 0);
				s=dft.format(calendar.getTime());
				}
			results.add(new ParameInfo(s,s));
		}
		return results;
	}

	public static void main(String[] args) {
		DATEMreplace d = new DATEMreplace();
		for (ParameInfo p : d.execParame("2014-01-31+2014-12-31+MM:1+type:1")) {
			System.out.println(p.getValue());
		}
	}
}
