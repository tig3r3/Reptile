package com.syntun.replace;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class DATENreplace implements replaceParameter {
	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		LinkedList<ParameInfo> result = new LinkedList<ParameInfo>();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		Date beginDate = new Date();
		Calendar date = Calendar.getInstance();
		date.setTime(beginDate);
		date.set(Calendar.DATE, date.get(Calendar.DATE) - 1);
		try {
			Date endDate = dft.parse(dft.format(date.getTime()));
			result.add(new ParameInfo(dft.format(endDate), dft.format(endDate)));
			System.out.println(dft.format(endDate));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

}
