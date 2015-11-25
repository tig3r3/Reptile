package com.syntun.replace;

import java.util.LinkedList;
import com.syntun.tools.JDConnectSql;

public class PJDreplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		String[] pArr = exeParam.split("-");
		int minNum = Integer.parseInt(pArr[0]);
		int maxNum = Integer.parseInt(pArr[1]);
		String productId = pArr[2];
		String tableName = pArr[3];
		int selectMax = JDConnectSql.getMaxNum(productId, tableName);
		if (selectMax > 5) {
			maxNum = maxNum - selectMax + 5;
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
