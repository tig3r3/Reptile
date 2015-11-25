package com.syntun.replace;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ureplace implements replaceParameter {

	@Override
	public LinkedList<ParameInfo> execParame(String exeParam) {
		if(!this.inspectParameData(exeParam)) return null;
		LinkedList<ParameInfo> ll = new LinkedList<ParameInfo>();
		try {
			ll.add(new ParameInfo("pinpaiSearchU",URLEncoder.encode(exeParam,"UTF-8")));
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
	
	/**
	 * 测试 main
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		String urlStr = "http://detail.tmall.com/venus/spu_detail.htm?spu_id=123709897&entryNum=0&mallstItemId=9139163619&default_item_id=13449516329&q=%C6%BD%B0%E5+%B5%E7%C4%D4&rewq=&#24179;&#26495;%20&#30005;&#33041;&rewcatid=&page_type=&from=sn_1_cat-qp&disp=g&active=1";
		try {
			URL url = new URL(urlStr);
			try {
				new URI(url.toExternalForm());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
