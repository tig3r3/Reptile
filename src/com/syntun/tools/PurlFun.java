package com.syntun.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PurlFun {
	/**
	 * 将字符进行两次UTF-8转码处理
	 * 
	 * @param str
	 * @return
	 */
	public String UnicodeToUtf8(String str) {
		String s1 = "";
		try {
			s1 = URLEncoder.encode(str, "UTF-8");
			s1 = URLEncoder.encode(s1, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s1;
	}

	public static void main(String[] args) {
		PurlFun p = new PurlFun();
		System.out.println(p.UnicodeToUtf8("+"));
	}
}
