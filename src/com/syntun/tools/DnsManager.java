package com.syntun.tools;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 
 * dns管理类，提供dns访问的ip和cookie列表
 * 
 * @author Zhenchong Li
 * 
 */
public class DnsManager {

	/**
	 * 根据给定URL发起CDN连接，如果未指定IP地址，随机返回一个请求地址
	 * 
	 * @param urlStr
	 *            - 需要发起CDN请求的url isGoIP - 是否需要走IP
	 * @return HttpURLConnection
	 * @throws Exception
	 */
	public static HashMap<String, String> getURLStr(String urlStr,
			boolean isGoIP) throws Exception {
		URL url1 = new URL(urlStr);
		String cookie = "";
		// cookie= getCookieStr(urlStr);
		if (isGoIP) {
			String hostStr = url1.getHost();
			ArrayList<String> ips = getCDNIPs(hostStr, false);
			String randomIP = getRandomIP(ips);
			urlStr = urlStr.replace(hostStr, randomIP);
		}
		System.out.println(urlStr + ":" + cookie);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("urlStr", urlStr);
		map.put("cookie", cookie);
		return map;
	}

	/* 缓存CDN IP地址列表 */
	private static HashMap<String, ArrayList<String>> cdnCachedIPs = new HashMap<String, ArrayList<String>>();
	/* 上次缓存刷新时间 */
	private static HashMap<String, Long> lastCachedTime = new HashMap<String, Long>();
	/* 缓存保存时间(毫秒), 默认1小时 */
	private static int cachedMilliseconds = 3600 * 1000;

	/* 缓存Cookie地址列表 */
	private static HashMap<String, String> cookieCachedIPs = new HashMap<String, String>();
	/* cookie上次缓存刷新时间 */
	private static HashMap<String, Long> lastcookieCachedTime = new HashMap<String, Long>();

	/**
	 * 读取当前缓存中有效的IP地址
	 * 
	 * @param host
	 * @return 缓存中有效的IP地址
	 */
	public static ArrayList<String> getCachedIPs(String host) {
		ArrayList<String> cachedIPs = new ArrayList<String>();
		if (lastCachedTime.containsKey(host)) {
			long currentTime = System.currentTimeMillis();
			long cachedTime = lastCachedTime.get(host);
			if ((currentTime - cachedTime) <= cachedMilliseconds) {
				cachedIPs = cdnCachedIPs.get(host);
			}
		}
		return cachedIPs;
	}

	/**
	 * 获取当前URL地址的CND IP地址列表
	 * 
	 * @param urlStr
	 *            - 需要获取CDN IP地址的URL
	 * @param refreshCache
	 *            - 是否强行刷新缓存
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<String> getCDNIPs(String host, boolean cacheRefresh)
			throws Exception {
		ArrayList<String> ipList = new ArrayList<String>();
		// 如果有缓存IP，从缓存中读取
		ArrayList<String> cachedIPs = getCachedIPs(host);
		if (cachedIPs.isEmpty() || cacheRefresh) {
			java.security.Security.setProperty("networkaddress.cache.ttl", "6");
			InetAddress addressesInet[] = InetAddress.getAllByName(host);
			for (InetAddress address : addressesInet) {
				String addressStr = address.getHostAddress();
				ipList.add(addressStr);
			}
			// 缓存IP
			setCachedIPs(host, ipList);
		} else {
			ipList = cachedIPs;
		}
		return ipList;
	}

	/**
	 * 缓存给定host的cdn ip
	 * 
	 * @param host
	 * @param ips
	 */
	public static void setCachedIPs(String host, ArrayList<String> ips) {
		long currentTime = System.currentTimeMillis();
		cdnCachedIPs.put(host, ips);
		lastCachedTime.put(host, currentTime);
	}

	/**
	 * 从IP列表中随机获取一个IP
	 * 
	 * @param cdnIPs
	 * @return ip
	 */
	public static String getRandomIP(ArrayList<String> cdnIPs) {
		System.out.println(cdnIPs.size());
		Random random = new Random();
		return cdnIPs.get(random.nextInt(cdnIPs.size()));
	}

	/**
	 * 从给定URL中获取cookie
	 * 
	 * @param urlStr
	 * @return cookieMap字符串 0 - 302跳转中得到的 1 - 直接得到的
	 * @throws Exception
	 */
	public static String getCookieStr(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		String cookieMap = "";
		long currentTime = System.currentTimeMillis();
		if (lastcookieCachedTime.containsKey(url.getHost())) {
			long cachedTime = lastCachedTime.get(url.getHost());
			if ((currentTime - cachedTime) <= cachedMilliseconds) {
				cookieMap = cookieCachedIPs.get(url.getHost());
				return cookieMap;
			}
		}

		String redirectURLStr = "";
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
			cookieMap = getCookieFromHeader(map);
			// get cookie end
			URL redirectUrl = new URL(redirectURLStr);
			uc = (HttpURLConnection) redirectUrl.openConnection();
			uc.setInstanceFollowRedirects(false);
			uc.setRequestMethod("GET");
			uc.connect();
		}

		cookieCachedIPs.put(url.getHost(), cookieMap);
		lastcookieCachedTime.put(url.getHost(), currentTime);
		return cookieMap;
	}

	/**
	 * 从响应头获取cookie信息
	 * 
	 * @param headerFields
	 * @return cookie信息
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
			}
		}
		return cookie;
	}

}
