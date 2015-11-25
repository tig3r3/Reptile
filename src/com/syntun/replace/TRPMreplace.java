package com.syntun.replace;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class TRPMreplace implements replaceParameter {

	public LinkedList<ParameInfo> execParame(String exeParam) {
		if (!this.inspectParameData(exeParam))
			return null;
		String[] pArr = exeParam.split("-");
		// String str ="";
		int pageTotal = 0;
		if (pArr[0].indexOf("u4e07") > 0) {
			pageTotal = 10000;
		} else
			pageTotal = Integer.parseInt(pArr[0]);

		// int pageTotal = Integer.parseInt(pArr[0]);
		int pageSize = Integer.parseInt(pArr[1]);
		int page = pageTotal / pageSize + 1;
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		if (page > 100) {
			page = 100;
		}
		for (int i = 1; i < page; i++) {
			int s = i * pageSize;
			ll.add(new ParameInfo(s + "", s + ""));
		}
		return ll;
	}

	/**
	 * ���ֵ�Ƿ��Ϲ淶
	 * 
	 * @param exeParam
	 * @return
	 */
	public boolean inspectParameData(String exeParam) {
		String inspectParamStr = "\\d*?.*?\\-\\d+?";
		return Pattern.matches(inspectParamStr, exeParam);
	}

	public static void main(String[] args) {
		String mm = "9.0\u4e07-36";

		new TRPMreplace().execParame(mm);

		System.out.println("11");
	}
}
