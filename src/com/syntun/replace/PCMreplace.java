package com.syntun.replace;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class PCMreplace implements replaceParameter {
//1-1000-10-50
	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if(!this.inspectParameData(exeParam)) return null;
		String[] pArr = exeParam.split("-");
		int minNum = Integer.parseInt(pArr[0]);
		int pageShowNum = Integer.parseInt(pArr[2]);
		int maxPageNum = Integer.parseInt(pArr[3]);
		int maxNum = (int)Math.ceil((Double.parseDouble(pArr[1])/pageShowNum));
		maxPageNum = maxPageNum>maxNum?maxNum:maxPageNum;
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		if(minNum<=maxNum)
			for(int i=minNum;i<=maxPageNum;i++){
				ll.add(new ParameInfo(i+"",i+""));
			}
		else ll = null;
		return ll;
		
	}
	/**
	 * 检查值是否符合规范
	 * @param exeParam
	 * @return
	 */
	public boolean inspectParameData(String exeParam){
		String inspectParamStr = "\\d+?\\-\\d+?-\\d+?-\\d+?";
		return Pattern.matches(inspectParamStr,exeParam);
	}
}
