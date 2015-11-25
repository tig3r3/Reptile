package com.syntun.replace;

import java.util.LinkedList;

import com.syntun.tools.JDConnectSql;

public class PCJDreplace implements replaceParameter {
	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		String[] pArr = exeParam.split("-");
		int minNum = Integer.parseInt(pArr[0]);
		int pageShowNum = Integer.parseInt(pArr[2]);
		int maxNum = (int) Math
				.ceil((Double.parseDouble(pArr[1]) / pageShowNum));

		String productId = pArr[3];
		String tableName = pArr[4];
		int selectNum = (int) Math.ceil(JDConnectSql.getMaxNum(productId,
				tableName) / pageShowNum);
		if (selectNum > 5) {
			maxNum = maxNum - selectNum + 5;
		}

		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		if (minNum <= maxNum)
			for (int i = minNum; i <= maxNum; i++) {
				ll.add(new ParameInfo(i + "", i + ""));
			}
		else
			ll = null;
		return ll;

	}

}
