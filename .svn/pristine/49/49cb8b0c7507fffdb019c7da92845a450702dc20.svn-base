package com.syntun.tools;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SyntunDate {
	//{PCM--1-[total_sq]-15-100--}
	public static ArrayList<String> getInterDays(String startDay, String endDay)
	{
		Pattern pt = Pattern.compile("[\\d]{4}\\-[\\d]{2}\\-[\\d]{2}");
		Matcher sm = pt.matcher(startDay);
		if(!sm.find()) {
			System.out.println("error:ss");
			System.exit(0);
		}
		Matcher em = pt.matcher(endDay);
		if(!em.find()) {
			System.out.println("error:ss");
			System.exit(0);
		}
		ArrayList<String> result = new ArrayList<String>();
		if(startDay.equals(endDay)) {
			result.add(startDay);
		} else {
			DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
			DateTime startDate = DateTime.parse(startDay);
			DateTime endDate = DateTime.parse(endDay);
			Days InterDays = Days.daysBetween(startDate, endDate);
			
			int days = InterDays.getDays();
			for(int i = 0; i <= days; i++) {
				result.add(startDate.plusDays(i).toString(dtf));
			}
		}
		return result;
	}
}
