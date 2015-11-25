package com.syntun.replace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UGreplace implements replaceParameter {
	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if(!this.inspectParameData(exeParam)) return null;
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		try {
			ll.add(new ParameInfo("pinpaiSearchU",URLEncoder.encode(exeParam,"GBK")));
		} catch (UnsupportedEncodingException e) {
			ll=null;
		}
		return ll;
	}
	
	/**
	 * 检查值是否符合规范
	 * @param exeParam
	 * @return
	 */
	public boolean inspectParameData(String exeParam){
		String inspectParamStr = "\\[.*?\\]";
		Pattern p = Pattern.compile(inspectParamStr);
		Matcher m = p.matcher(exeParam);
		return !m.find();
	}
}
