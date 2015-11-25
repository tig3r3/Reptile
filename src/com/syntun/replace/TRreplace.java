package com.syntun.replace;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class TRreplace  implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if(!this.inspectParameData(exeParam)) return null;
		String[] pArr = exeParam.split("-");
		int pageTotal = Integer.parseInt(pArr[0]);
		int pageSize = Integer.parseInt(pArr[1]);
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		for(int i=1;i<pageTotal;i++){
			int s=i*pageSize;
			ll.add(new ParameInfo(s+"",s+""));
		}
		return ll;
	}
	/**
	 * 检查值是否符合规范
	 * @param exeParam
	 * @return
	 */
	public boolean inspectParameData(String exeParam){
		String inspectParamStr = "\\d+?\\-\\d+?";
		return Pattern.matches(inspectParamStr,exeParam);
	}
}
