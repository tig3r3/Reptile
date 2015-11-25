package com.syntun.replace;

import java.util.LinkedList;
import com.syntun.tools.TMConnectSql;

public class PCMTMreplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		String[] pArr = exeParam.split("-");
		int minNum = Integer.parseInt(pArr[0]);
		int pageShowNum = Integer.parseInt(pArr[2]);
		int maxPageNum = Integer.parseInt(pArr[3]);
		String maxTotalStr = pArr[1];
		double maxTotal = 10000;
		if (maxTotalStr.indexOf("万") == -1) {
			maxTotal = Double.parseDouble(maxTotalStr);
		}
		int maxNum = (int) Math.ceil((maxTotal / pageShowNum));
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();

		String productId = pArr[4];
		String tableName = pArr[5];
		int selectNum = (int) Math.ceil(TMConnectSql.getMaxNum(productId,
				tableName) / pageShowNum);
		if (selectNum > 5) {
			maxNum = maxNum - selectNum + 5;
		}
		maxPageNum = maxPageNum > maxNum ? maxNum : maxPageNum;
		if (minNum <= maxNum)
			for (int i = minNum; i <= maxPageNum; i++) {
				ll.add(new ParameInfo(i + "", i + ""));
			}
		else
			ll = null;
		return ll;

	}
	// /**
	// * 检查值是否符合规范
	// *
	// * @param exeParam
	// * @return
	// */
	// public boolean inspectParameData(String exeParam) {
	// String inspectParamStr = "\\d+?\\-\\d+?-\\d+?-\\d+?";
	// return Pattern.matches(inspectParamStr, exeParam);
	// }
}
