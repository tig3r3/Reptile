package com.syntun.tools;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DnsTools {
	/**
	 * 从给定URL中获取cookie
	 * 
	 * @param urlStr
	 * @return cookieMap字符串 0 - 302跳转中得到的 1 - 直接得到的
	 * @throws Exception
	 */
	public static String getCookieStr(String urlStr) throws Exception {
		String cookieStr = "";
		String redirectURLStr = "";
		URL url = new URL(urlStr);
		HttpURLConnection uc = (HttpURLConnection) url.openConnection();
		uc.setInstanceFollowRedirects(false);
		uc.setRequestMethod("GET");
		uc.setRequestProperty(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.94 Safari/537.36");
		uc.connect();
		// loop in 302 status
		while (uc.getResponseCode() == 302) {
			redirectURLStr = uc.getHeaderField("Location");
			// get cookie begin
			Map<String, List<String>> map = uc.getHeaderFields();
			cookieStr = getCookieFromHeader(map);
			// get cookie end
			URL redirectUrl = new URL(redirectURLStr);
			uc = (HttpURLConnection) redirectUrl.openConnection();
			uc.setInstanceFollowRedirects(false);
			uc.setRequestMethod("GET");
			uc.connect();
		}
		return cookieStr;
	}

	/**
	 * @param headerFields
	 *            头字段
	 * @return
	 */
	private static String getCookieFromHeader(
			Map<String, List<String>> headerFields) {
		String cookie = "";
		Map<String, List<String>> map = headerFields;
		Set<String> set = map.keySet();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			if (key != null && key.equals("Set-Cookie")) {
				List<String> list = map.get(key);
				StringBuilder builder = new StringBuilder();
				for (String str : list) {
					builder.append(str).toString();
				}
				cookie = builder.toString();
				System.out.println("得到的cookie= " + cookie);
			}
		}
		return cookie;
	}
}
