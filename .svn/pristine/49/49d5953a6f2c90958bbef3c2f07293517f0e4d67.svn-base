package com.syntun.replace;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSreplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if(!this.inspectParameData(exeParam)) return null;
		String wayStr = inspectWay(exeParam);
		String[] pArr = exeParam.split("\\"+wayStr);
		int numA = Integer.parseInt(pArr[0]);
		int numB = Integer.parseInt(pArr[1]);
		long result = 0;
		LinkedList<ParameInfo> rs = new LinkedList<ParameInfo>();
		if("-".equals(wayStr)) {
			result = numA - numB;
		}
		if("+".equals(wayStr)) {
			result = numA + numB;
		}
		if("*".equals(wayStr)) {
			result = numA * numB;
		}
		if("/".equals(wayStr)) {
			result = numA / numB;
		}
		rs.add(new ParameInfo("result", result+""));
		
		return rs;
		
	}
	/**
	 * 检查值是否符合规范
	 * @param exeParam
	 * @return
	 */
	public boolean inspectParameData(String exeParam){
		String inspectParamStr = "\\d+?[^\\d\\s]{1}\\d+?";
		return Pattern.matches(inspectParamStr,exeParam);
	}
	/**
	 * 获取操作符
	 * @param exeParam
	 * @return
	 */
	public String inspectWay(String exeParam) {
		String pattStr = "\\d+?([^\\d\\s]{1})\\d+?";
		Matcher wayM = Pattern.compile(pattStr).matcher(exeParam);
		String wayStr = null;
		while(wayM.find()) {
			wayStr = wayM.group(1);
		}
		return wayStr;
	}
}
