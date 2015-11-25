package com.syntun.replace;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DATEreplace implements replaceParameter {
	// 1-1000-10-50
	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if (!this.inspectParameData(exeParam))
			return null;
		String[] pArr = exeParam.split("\\+");
		String start = pArr[0];
		String end = pArr[1];
		return getInterDays(start, end);

	}

	public static LinkedList<ParameInfo> getInterDays(String startDay,
			String endDay) {
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
			for (int i = 0; i <= days; i++) {
				result.add(new ParameInfo(startDate.plusDays(i).toString(dtf)
						+ "", startDate.plusDays(i).toString(dtf) + ""));
			}
		}
		return result;
	}

	/**
	 * 检查值是否符合规范
	 * 
	 * @param exeParam
	 * @return
	 */
	public boolean inspectParameData(String exeParam) {
		String inspectParamStr = "\\d+?-\\d+?-\\d+?\\+\\d+?-\\d+?-\\d+?";
		return Pattern.matches(inspectParamStr, exeParam);
	}

	public static void main(String[] args) {
		DATEreplace d = new DATEreplace();
		for (ParameInfo p : d.execParame("2014-11-12+2014-12-12")) {
			System.out.println(p.getValue());
		}

	}
}
