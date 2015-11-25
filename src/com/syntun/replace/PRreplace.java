﻿package com.syntun.replace;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class PRreplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if(!this.inspectParameData(exeParam)) return null;
		String[] pArr = exeParam.split("-");
		int minNum = Integer.parseInt(pArr[0]);
		int pageShowNum = Integer.parseInt(pArr[2]);
		int maxNum = Math.round((Integer.parseInt(pArr[1])/pageShowNum));
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		if(minNum<=maxNum)
			for(int i=0;i<=maxNum;i++){
				ll.add(new ParameInfo(i+"",i*pageShowNum+""));
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
		String inspectParamStr = "\\d+?\\-\\d+?-\\d+?";
		return Pattern.matches(inspectParamStr,exeParam);
	}

}
