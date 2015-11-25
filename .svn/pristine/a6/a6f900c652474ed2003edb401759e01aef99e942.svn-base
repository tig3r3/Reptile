package com.syntun.webget;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import com.syntun.tools.Base64;
import com.syntun.tools.ConnADSL;
import com.syntun.tools.ConnectSql;
import com.syntun.tools.PatternMatcher;
import com.syntun.tools.PurlFiledList;
import com.syntun.tools.PurlFun;
import com.syntun.tools.SleepUtil;
import com.syntun.wetget.annotation.SetProperty;

/**
 * 
 * 多域页面漫爬抓取类
 * 
 */
@SetProperty(propertyVars = { "maximumPoolSize", "timeOut",
		"urlMaxResetAcessNum", "resetAdslIOENum", "proxyHost", "proxyPort",
		"proxyUser", "proxyPass", "cookieTime" }, propertyComment = "url请求参数")
public class GetWebPage {
	public static List<PurlFiledList> listPurlFiled = new ArrayList<PurlFiledList>();

	public static int timeOut = 30 * 1000;
	/**
	 * 启动次数
	 */
	private static int startNum = 0;
	/**
	 * 线程池最大线程数量
	 */
	private static int maximumPoolSize = 8;
	public static int cookieTime = 5;

	/**
	 * 是否正在连接ADSL
	 */
	private static boolean isConnAdsl = false;
	/**
	 * 同一地址最大重复访问次数
	 */
	private static int urlMaxResetAcessNum = 2;
	/**
	 * 最大读取长度(2M)
	 */
	final static long MAX_READER_LENGTH = 1000 * 5000;
	// 代理地址
	private static String proxyHost = "192.168.0.200";
	// 代理端口
	private static int proxyPort = 65500;
	// 代理用户名
	public static String proxyUser = "u1";
	// 代理密码
	private static String proxyPass = null;
	private static List<ProxyUser> proxyU = new ArrayList<ProxyUser>();

	/**
	 * 发生几次io异常后重新连接adsl
	 */
	private static int resetAdslIOENum = 10;
	/**
	 * 当前adsl重起之前IO异常次数
	 */
	private static int iOENum = 0;

	/**
	 * 获取cookie
	 */
	public static Map<String, String> cookieMap = new HashMap<String, String>();

	private static int proxyNum = 0;

	/**
	 * 获取https 证书处理
	 */
	// public static List<String> htttpsCfMap = new ArrayList<String>();

	// private static HostnameVerifier defaultHostnameVerifier;
	// private static

	// static {
	// defaultHostnameVerifier = new DefaultHostnameVerifier();
	// hv = new HostnameVerifier() {
	// @Override
	// public boolean verify(String hostname, SSLSession session) {
	// return true;
	// }
	// };
	// }

	// private static class DefaultHostnameVerifier implements HostnameVerifier
	// {
	// public boolean verify(String hostname, SSLSession session) {
	// return false;
	// }
	// }

	/**
	 * 断线，后判断是否重新连接
	 */
	private static synchronized void isOutLine() {
		iOENum++;
		// 是否进行adsl重起
		if (iOENum > resetAdslIOENum)
			iOENum = 0;
		else
			return;
		// 重起adsl
		System.out.println("********:now start reset ADSL conection .......");
		isConnAdsl = true;
		if (proxyPass != null && !proxyPass.equals("")) {
			proxyNum++;
			if (proxyNum == proxyU.size()) {
				proxyNum = 0;
			}
			proxyUser = proxyU.get(proxyNum).getUserName();
			proxyPass = proxyU.get(proxyNum).getUserPwd();
			ConnADSL.connAgent(proxyU.get(proxyNum).getPort());
		} else {
			while (!ConnADSL.connADSL()) {
				SleepUtil.sleep(1000);
			}
		}
		isConnAdsl = false;
		System.out.println("********:end reset ADSL conectioned ......."
				+ proxyUser);
	}

	/**
	 * 单件模式私有构造函数
	 */
	private GetWebPage() {
	}

	public static HashMap<String, List<String>> cookieList = new HashMap<String, List<String>>();

	public static void getCook() {
		Connection conn = (Connection) ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			// 加载地址过滤正则
			String sql = "select * from url_cookie";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String key = rs.getString("url_group");
				cookieMap.put(key, rs.getString("cookie_str"));
			}
			rs.close();
			stmt.close();
			PreparedStatement pst = conn
					.prepareStatement("SELECT `user_name`, `user_pwd`,`port` FROM proxy_user WHERE STATUS=1");
			ResultSet rsP = pst.executeQuery();
			while (rsP.next()) {
				ProxyUser user = new ProxyUser();
				user.setPort(rsP.getInt("port"));
				user.setUserName(rsP.getString("user_name"));
				user.setUserPwd(rsP.getString("user_pwd"));
				proxyU.add(user);
			}
			rsP.close();
			pst.close();
			Statement stmt1 = conn.createStatement();
			// 加载地址过滤正则
			String sql1 = "select * from url_cookie_list";
			ResultSet rs1 = stmt1.executeQuery(sql1);
			while (rs1.next()) {
				String key = rs1.getString("url_group");
				String[] strs = rs1.getString("sort_id").split(",");
				if (strs.length != 1) {
					for (String str : strs) {
						key = key + "_" + str;
						List<String> list = null;
						if (!cookieList.containsKey(key)) {
							list = new ArrayList<String>();
						} else {
							list = cookieList.get(key);
						}
						list.add(rs1.getString("url_cookie"));
						cookieList.put(key, list);
					}
				} else {
					key = key + "_" + strs[0];
					List<String> list = null;
					if (!cookieList.containsKey(key)) {
						list = new ArrayList<String>();
					} else {
						list = cookieList.get(key);
					}
					list.add(rs1.getString("url_cookie"));
					cookieList.put(key, list);
				}
			}
			rs1.close();
			stmt1.close();
			// 查询https 证书表
			// PreparedStatement ps = conn
			// .prepareStatement("SELECT `url_group`, `sort_id` FROM https_cf");
			// ResultSet rps = ps.executeQuery();
			// while (rps.next()) {
			// htttpsCfMap.add(rps.getString("url_group") + "_"
			// + rps.getString("sort_id"));
			// }
			// rps.close();
			// ps.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.exit(0);
		}
		ConnectSql.push(conn);
	}

	public static List<String> url411 = new ArrayList<String>();

	/**
	 * 开始抓取
	 * 
	 * @throws SQLException
	 */
	public static void startGet() {
		getCook();
		getPurlFiledList();
		getUrl302List();
		getUrl411();
		if (GetWebPage.startNum == 0) {
			for (int i = 0; i < GetWebPage.maximumPoolSize; i++) {
				GetWebPage gpc = new GetWebPage();
				new Thread(gpc.new GetPage()).start();
				startNum++;
			}
		}
	}

	public static void getUrl411() {
		// 查询表名称
		String sql = "SELECT url_addr FROM wgdata.url_err411";
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				url411.add(rs.getString("url_addr"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	public static void getPurlFiledList() {
		// 查询表名称
		String sql = "SELECT `url_group`, `generate_sort_id`, `field_str`, `filed_fun` FROM purl_filed_list";
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// System.out.println(rs.getString("filed_fun"));
				PurlFiledList p = new PurlFiledList();
				p.setFieldStr(rs.getString("field_str"));
				p.setFiledFun(rs.getString("filed_fun"));
				p.setGenerateSortId(rs.getString("generate_sort_id"));
				p.setUrlGroup(rs.getString("url_group"));
				listPurlFiled.add(p);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	public static List<Url302List> listUrl302 = new ArrayList<Url302List>();

	public static void getUrl302List() {
		// 查询表名称
		String sql = "SELECT `url_group`, `sort_id` FROM url302list";
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Url302List p = new Url302List();
				p.setUrlGroup(rs.getInt("url_group"));
				p.setSortId(rs.getString("sort_id"));
				listUrl302.add(p);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	/**
	 * 完成请求并取的网页内容的内部类
	 */
	class GetPage implements Runnable {
		@Override
		protected void finalize() throws Throwable {
			super.finalize();
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			Url ui = null;
			int urlAcessNum = 0;
			while (true) {
				// if (ui != null && ui.getPatternUrlId() == 500001) {
				// UserDangdang.adduserUrlList(ui);
				// UserDangdang.updateStatusToTrue();
				// ui = null;
				// }
				if (ui == null || urlAcessNum >= urlMaxResetAcessNum) {
					ui = UrlSupervise.getUrlInfo();
					urlAcessNum = 0;
				} else
					urlAcessNum++;
				if (ui != null) {
					String key = ui.getPatternUrlId() + "_" + ui.getSortId();
					if (cookieList.containsKey(key)
							&& cookieList.get(key).size() > 0) {
						ui.setCookie(cookieList.get(key).get(0));
					}
					try {
						while (GetWebPage.isConnAdsl)
							SleepUtil.sleep(1000 * 2);
						long bTime = System.currentTimeMillis();
						if (ui.getUrl().indexOf("https") == 0) {
							this.getPageInfoHttps(ui);
						} else {
							this.getPageInfo(ui);
						}
						if (ui.getHtmlContent() == null
								&& (ui.getPatternUrlId() == 2 || ui
										.getPatternUrlId() == 3)) {
							ui = null;
							continue;
						}
						long eTime = System.currentTimeMillis();
						System.out.println(ui.getUrl() + " getTime = "
								+ (eTime - bTime));
						int resultCode = InspectWebPage.getInspectObj()
								.inspectPage(ui);
						if (ui.getUrl().indexOf("mdskip.taobao.com") != -1
								&& ui.getHtmlContent().indexOf(
										"window.location.href=") != -1) {
							continue;
						}
						if (resultCode == 1) {
							throw new IOException("可能被封IP!!!");
						} else if (resultCode == 2) {
							Url rUi = new Url(ui.getPreantUrl(), ui);
							if (rUi.getUrl().indexOf("https") == 0) {
								this.getPageInfoHttps(rUi);
							} else {
								this.getPageInfo(rUi);
							}
							continue;
						}

						for (UrlLimit ul : UrlLimitInit.urlLimitList) {
							if (ul.getUrlGroup().equals(
									ui.getPatternUrlId() + "")) {
								for (String s : ul.getSortIdList()) {
									if (s.equals(ui.getSortId() + "")) {
										ul.updateStatusToTrue();
										System.out.println("parse:"
												+ ul.getUrlGroup() + ":" + s);
										break;
									}
								}
							}
						}
						ui.addData("download_time", new Date().getHours() + "");
						// 判断是否是数据魔方的页面，如果是，从url地址中取出data_time值
						if (ui.getUrl().indexOf("mofang") != -1) {
							Pattern p = Pattern.compile("r1\\:(\\S*?)/");
							Matcher m = p.matcher(ui.getUrl());
							while (m.find()) {
								ui.addData("data_time", m.group(1));
							}
						}
						while (true) {
							if (ResolveWebPage.addContent(ui))
								break;
							else
								SleepUtil.sleep(Math.round(1000 * 0.5));
						}
					} catch (IOException ioe) {
						System.out.println("ioeMesage:" + ioe.getMessage());
						if (!GetWebPage.isConnAdsl)
							GetWebPage.isOutLine();
						System.out.println("获取内容错误 = " + ui.getUrl());
						// if (ui != null && ui.getPatternUrlId() == 500001) {
						// UserDangdang.adduserUrlList(ui);
						// UserDangdang.updateStatusToTrue();
						// ui = null;
						// continue;
						// }
						boolean isLimit = true;
						for (UrlLimit ul : UrlLimitInit.urlLimitList) {
							if (ul.getUrlGroup().equals(
									ui.getPatternUrlId() + "")) {
								for (String s : ul.getSortIdList()) {
									if (s.equals(ui.getSortId() + "")) {
										isLimit = false;
										ul.updateStatusToTrue();
										ul.getUrlLimitThread().adduserUrlList(
												ui);
										break;
									}
								}
							}
						}
						if (!isLimit) {
							ui = null;
							continue;
						}
						while (GetWebPage.isConnAdsl)
							SleepUtil.sleep(1000 * 2);
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(0);
					}
					ui = null;
				} else {
					SleepUtil.sleep(1000 * 1);
				}
			}
		}

		/**
		 * 获取页面内容
		 * 
		 * @return
		 * @throws IOException
		 * @throws InterruptedException
		 * @throws Exception
		 */
		public void getPageInfo(Url ui) throws IOException,
				InterruptedException {
			if (ui.getUrl().contains("yhd")
					&& (ui.getUrl().contains("vId") || ui.getUrl().contains(
							"uid"))) {
				String[] urlStrs = ui.getUrl().split("&");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < urlStrs.length; i++) {
					String value = urlStrs[i];
					if (value.contains("vId") || value.contains("uid")) {
						PurlFun pf = new PurlFun();
						try {
							String s[] = urlStrs[i].split("=");
							value = s[0]
									+ "="
									+ pf.UnicodeToUtf8(s[1].replace("%253D",
											"="));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (i == urlStrs.length - 1) {
						sb.append(value);
					} else {
						sb.append(value).append("&");
					}
				}
				ui.setUrl(sb.toString());
			}
			URL url1 = new URL(ui.getUrl());
			boolean isLimit = true;
			for (UrlLimit ul : UrlLimitInit.urlLimitList) {
				if (ul.getUrlGroup().equals(ui.getPatternUrlId() + "")) {
					for (String s : ul.getSortIdList()) {
						if (s.equals(ui.getSortId() + "")) {
							isLimit = false;
							break;
						}
					}
				}
				if (!isLimit && ul.getIsDns() == 0) {
					isLimit = true;
				} else if (!isLimit) {
					System.out.println("延迟加载：" + ui.getUrl());
				}
			}
			if (!isLimit) {
				HashMap<String, String> urlCookie = DnsManager.getURLStr(
						ui.getUrl(), true);
				String urlStr = urlCookie.get("urlStr");
				System.out.println("延迟加载并且使用ip：" + urlStr);
				ui.setUrl(urlStr);
			}
			URL url = new URL(ui.getUrl());
			HttpURLConnection uc = null;
			if (proxyPass != null && !proxyPass.equals("")) {
				// 指定代理服务器地址和端口
				InetSocketAddress isa = new InetSocketAddress(proxyHost,
						proxyPort);
				// 创建代理
				Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
				// 设置登陆到代理服务器的用户名和密码
				Authenticator.setDefault(new MyAuthenticator(proxyUser,
						proxyPass));
				uc = (HttpURLConnection) url.openConnection(proxy);
			} else {
				uc = (HttpURLConnection) url.openConnection();
			}

			uc.setConnectTimeout(GetWebPage.timeOut);
			uc.setDoOutput(true);
			uc.setUseCaches(false);
			boolean is411 = false;
			for (String u : url411) {
				if (ui.getUrl().indexOf(u) != -1) {
					uc.setRequestMethod("GET");
					is411 = true;
				}
			}
			if (!is411) {
				if (ui.getPatternUrlId() == 989) {
					uc.setRequestMethod("POST");
				} else {
					uc.setRequestMethod("GET");
				}
			}
			uc.setRequestProperty("Host", url1.getHost());
			uc.setRequestProperty("Keep-Alive", "300");
			uc.setRequestProperty("Accept", "*/*");
			if (ui.getPreantUrl() != null && !ui.getPreantUrl().equals("")
					&& isLimit)
				uc.setRequestProperty("Referer", ui.getPreantUrl().trim());
			uc.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			uc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			uc.setRequestProperty("Cache-Control", "max-age=0");
			uc.setRequestProperty("Connection", "keep-alive");
//			if (ui.getUrl().indexOf("mdskip.taobao.com") != -1) {
//				uc.setRequestProperty("User-Agent",
//						"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
//			}
//			else if (ui.getUrl().indexOf(
//					"www.suning.com/webapp/wcs/stores/ItemPrice") != -1
//					|| ui.getUrl().indexOf("www.suning.com/emall") != -1
//					|| ui.getUrl().indexOf("review.suning.com/ajax") != -1) {
//				uc.setRequestProperty("User-Agent",
//						"Baiduspider+(+http://www.baidu.com/search/spider.htm)");
//			}
//			else 
				if (ui.getPatternUrlId() == 100003) {
				uc.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64; compatible; MSIE 5.0; DigExt) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2194.2 Safari/537.36");
			} else {
				uc.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
			}
			String uiCookie = cookieMap.get(ui.getPatternUrlId() + "");
			if (ui.getCookie() != null) {
				uiCookie = ui.getCookie();
			}
			if (ui.getUrl().indexOf("mdskip.taobao.com") != -1) {
				String num = "";
				for (int i = 0; i < 32; i++) {
					if (Math.random() * 10 > 5) {
						num += (char) ((int) 'a' + (int) (Math.random() * 26));
					} else {
						num += (int) (Math.random() * 10);
					}
				}
				// System.out.println(num);
				// cookieMap.put("4", "t=" + num);
				uiCookie = "t=" + num;
			}

			if (uiCookie != null && isLimit) {
				uc.setRequestProperty("Cookie", uiCookie);
			}

			// if (ui.getPatternUrlId() == 5) {
			// uiCookie = GetTmallCookie.getCookie();
			// System.out.println(uiCookie);
			// if (uiCookie != null) {
			// uc.setRequestProperty("Cookie", uiCookie);
			// }
			// }

			uc.setReadTimeout(GetWebPage.timeOut);
			if (uc.getResponseCode() == 302) {
				for (Url302List u : listUrl302) {
					if (ui.getPatternUrlId() == Integer
							.valueOf(u.getUrlGroup())) {
						String[] strs302 = u.getSortId().split(",");
						System.out.println(strs302[0]);
						for (String str : strs302) {
							if (ui.getSortId() == Integer.valueOf(str)) {
								ui.setUrl(uc.getHeaderField("location"));
								System.out.println("302url:"
										+ uc.getHeaderField("location"));
								if (ui.getUrl().indexOf("https") == 0) {
									this.getPageInfoHttps(ui);
								} else {
									this.getPageInfo(ui);
								}
								return;
							}
						}
					}
				}
				throw new IOException("请求被跳转,断线重连");
			}
			if (uc.getResponseCode() == 500
					&& ui.getUrl().indexOf("pageadword") != -1
					&& (ui.getPatternUrlId() == 2 || ui.getPatternUrlId() == 3)) {
				UrlSupervise.addParseUrlInfo(ui);
				return;
			}
			// 取得字符集编码
			String charSet = null;
			String contentType = uc.getHeaderField("Content-Type");
			ui.setCookie(uc.getHeaderField("Set-Cookie"));
			if (contentType != null && !contentType.equals("")) {
				String[] contentTypeArr = contentType.split(";");
				for (int i = 0; i < contentTypeArr.length; i++) {
					if (contentTypeArr[i].indexOf("charset=") != -1) {
						String[] charSetArr = contentTypeArr[i].split("=");
						charSet = charSetArr[1].trim();
						break;
					}
				}
			}
			if (charSet == null)
				charSet = ui.getChartSet();
			else
				ui.setChartSet(charSet);
			// if ((ui.getPatternUrlId() == 2 || ui.getPatternUrlId() == 3)
			// && ui.getChartSet().toLowerCase().indexOf("gb") != -1
			// && ui.getSortId() != 1 && UrlSupervise.isFirst == 1) {
			// charSet = "UTF-8";
			// ui.setChartSet("UTF-8");
			// } else if ((ui.getPatternUrlId() == 2 || ui.getPatternUrlId() ==
			// 3)
			// && ui.getChartSet().toLowerCase().indexOf("UTF-8") != -1
			// && ui.getSortId() != 1 && UrlSupervise.isFirst == 1) {
			// charSet = "GBK";
			// ui.setChartSet("GBK");
			// }
			// 当字符集为GB2312时转成父集GBK
			charSet = charSet.equalsIgnoreCase("GB2312") ? "GBK" : charSet;
			InputStream uis = null;
			try {
				uis = uc.getInputStream();
			} catch (IOException e) {
				if (ui.getPatternUrlId() == 100003
						&& (ui.getSortId() == 68 || ui.getSortId() == 69)) {
					ui.setHtmlContent("--");
					return;
				}
				if (UrlSupervise.isFirst != 0) {
					throw e;
				} else {
					ui.setHtmlContent("您访问的网页不存在");
					return;
				}
			}
			InputStreamReader reader;
			StringBuffer htmlStr = new StringBuffer();
			// 检测返回的页面源码是否被压缩
			try {
				if (uc.getHeaderField("Content-Encoding") != null
						&& uc.getHeaderField("Content-Encoding")
								.equalsIgnoreCase("gzip"))
					reader = new InputStreamReader(new GZIPInputStream(uis),
							charSet);
				else
					reader = new InputStreamReader(uis, charSet);
				int readNum = 0;
				while (true) {
					int b = reader.read();
					if (b == -1 || readNum > MAX_READER_LENGTH)
						break;
					htmlStr.append((char) b);
					readNum++;
				}
				reader.close();

			} catch (EOFException eex) {
				eex.printStackTrace();
			}
			ui.setHtmlContent(htmlStr.toString().replace("\r\n", "")
					.replace("\n", "").replace("\r", ""));
		}

		/**
		 * 获取页面内容
		 * 
		 * @return
		 * @throws IOException
		 * @throws InterruptedException
		 * @throws Exception
		 */
		public void getPageInfoHttps(Url ui) throws IOException,
				InterruptedException {
			if (ui.getUrl().contains("yhd")
					&& (ui.getUrl().contains("vId") || ui.getUrl().contains(
							"uid"))) {
				String[] urlStrs = ui.getUrl().split("&");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < urlStrs.length; i++) {
					String value = urlStrs[i];
					if (value.contains("vId") || value.contains("uid")) {
						PurlFun pf = new PurlFun();
						try {
							String s[] = urlStrs[i].split("=");
							value = s[0]
									+ "="
									+ pf.UnicodeToUtf8(s[1].replace("%253D",
											"="));
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					if (i == urlStrs.length - 1) {
						sb.append(value);
					} else {
						sb.append(value).append("&");
					}
				}
				ui.setUrl(sb.toString());
			}
			URL url1 = new URL(ui.getUrl());
			boolean isLimit = true;
			for (UrlLimit ul : UrlLimitInit.urlLimitList) {
				if (ul.getUrlGroup().equals(ui.getPatternUrlId() + "")) {
					for (String s : ul.getSortIdList()) {
						if (s.equals(ui.getSortId() + "")) {
							isLimit = false;
							break;
						}
					}
				}
				if (!isLimit && ul.getIsDns() == 0) {
					isLimit = true;
				} else if (!isLimit) {
					System.out.println("延迟加载：" + ui.getUrl());
				}
			}
			if (!isLimit) {
				HashMap<String, String> urlCookie = DnsManager.getURLStr(
						ui.getUrl(), true);
				String urlStr = urlCookie.get("urlStr");
				System.out.println("延迟加载并且使用ip：" + urlStr);
				ui.setUrl(urlStr);
			}
			URL url = new URL(ui.getUrl());
			/**
			 * https 证书处理位置
			 */
			// if (htttpsCfMap.contains(ui.getPatternUrlId() + "_" +
			// ui.getSortId())) {
			// try {
			// trustAllHttpsCertificates();
			// } catch (Exception e1) {
			// e1.printStackTrace();
			// }
			// HostnameVerifier hv = new HostnameVerifier() {
			// @Override
			// public boolean verify(String hostname, SSLSession session) {
			// return true;
			// }
			// };
			// HttpsURLConnection.setDefaultHostnameVerifier(hv);
			// } else {
			// HttpsURLConnection
			// .setDefaultHostnameVerifier(defaultHostnameVerifier);
			// }
			/**
			 * https 证书处理结束
			 */
			HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
			;
			// if (proxyPass != null && !proxyPass.equals("")) {
			// // 指定代理服务器地址和端口
			// InetSocketAddress isa = new InetSocketAddress(proxyHost,
			// proxyPort);
			// // 创建代理
			// Proxy proxy = new Proxy(Proxy.Type.HTTP, isa);
			// // 设置登陆到代理服务器的用户名和密码
			// Authenticator.setDefault(new MyAuthenticator(proxyUser,
			// proxyPass));
			// uc = (HttpURLConnection) url.openConnection(proxy);
			// } else {
			// uc = (HttpURLConnection) url.openConnection();
			// }
			uc.setHostnameVerifier(new CustomizedHostnameVerifier());
			System.setProperty("https.protocols", "SSLv3,SSLv2Hello");
			uc.setConnectTimeout(GetWebPage.timeOut);
			uc.setDoOutput(true);
			uc.setUseCaches(false);
			boolean is411 = false;
			for (String u : url411) {
				if (ui.getUrl().indexOf(u) != -1) {
					uc.setRequestMethod("GET");
					is411 = true;
				}
			}
			if (!is411) {
				if (ui.getPatternUrlId() == 989) {
					uc.setRequestMethod("POST");
				} else {
					uc.setRequestMethod("GET");
				}
			}
			uc.setRequestProperty("Host", url1.getHost());
			uc.setRequestProperty("Keep-Alive", "300");
			uc.setRequestProperty("Accept", "*/*");
			if (ui.getPreantUrl() != null && !ui.getPreantUrl().equals("")
					&& isLimit)
				uc.setRequestProperty("Referer", ui.getPreantUrl().trim());
			uc.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			uc.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			uc.setRequestProperty("Cache-Control", "max-age=0");
			uc.setRequestProperty("Connection", "keep-alive");
			if (ui.getUrl().indexOf("mdskip.taobao.com") != -1) {
				uc.setRequestProperty("User-Agent",
						"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");
			} else if (ui.getUrl().indexOf(
					"www.suning.com/webapp/wcs/stores/ItemPrice") != -1
					|| ui.getUrl().indexOf("www.suning.com/emall") != -1
					|| ui.getUrl().indexOf("review.suning.com/ajax") != -1) {
				uc.setRequestProperty("User-Agent",
						"Baiduspider+(+http://www.baidu.com/search/spider.htm)");
			} else if (ui.getPatternUrlId() == 100003) {
				uc.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.3; WOW64; compatible; MSIE 5.0; DigExt) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2194.2 Safari/537.36");
			} else {
				uc.setRequestProperty(
						"User-Agent",
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36");
			}
			String uiCookie = cookieMap.get(ui.getPatternUrlId() + "");
			if (ui.getCookie() != null) {
				uiCookie = ui.getCookie();
			}
			if (ui.getUrl().indexOf("mdskip.taobao.com") != -1) {
				// String cookie = "";
				String num = "";
				for (int i = 0; i < 32; i++) {
					if (Math.random() * 10 > 5) {
						num += (char) ((int) 'a' + (int) (Math.random() * 26));
					} else {
						num += (int) (Math.random() * 10);
					}
				}
				System.out.println("t=" + num);
				// cookieMap.put("4", "t=" + num);
				uiCookie = "t=" + num;
			}

			if (uiCookie != null && isLimit) {
				uc.setRequestProperty("Cookie", uiCookie);
			}

			// if (ui.getPatternUrlId() == 5) {
			// uiCookie = GetTmallCookie.getCookie();
			// System.out.println(uiCookie);
			// if (uiCookie != null) {
			// uc.setRequestProperty("Cookie", uiCookie);
			// }
			// }

			uc.setReadTimeout(GetWebPage.timeOut);
			if (uc.getResponseCode() == 302) {
				for (Url302List u : listUrl302) {
					if (ui.getPatternUrlId() == Integer
							.valueOf(u.getUrlGroup())) {
						String[] strs302 = u.getSortId().split(",");
						System.out.println(strs302[0]);
						for (String str : strs302) {
							if (ui.getSortId() == Integer.valueOf(str)) {
								ui.setUrl(uc.getHeaderField("location"));
								System.out.println("302url:"
										+ uc.getHeaderField("location"));
								if (ui.getUrl().indexOf("https") == 0) {
									this.getPageInfoHttps(ui);
								} else {
									this.getPageInfo(ui);
								}
								return;
							}
						}
					}
				}
				throw new IOException("请求被跳转,断线重连");
			}
			if (uc.getResponseCode() == 500
					&& ui.getUrl().indexOf("pageadword") != -1
					&& (ui.getPatternUrlId() == 2 || ui.getPatternUrlId() == 3)) {
				UrlSupervise.addParseUrlInfo(ui);
				return;
			}
			// 取得字符集编码
			String charSet = null;
			String contentType = uc.getHeaderField("Content-Type");
			ui.setCookie(uc.getHeaderField("Set-Cookie"));
			if (contentType != null && !contentType.equals("")) {
				String[] contentTypeArr = contentType.split(";");
				for (int i = 0; i < contentTypeArr.length; i++) {
					if (contentTypeArr[i].indexOf("charset=") != -1) {
						String[] charSetArr = contentTypeArr[i].split("=");
						charSet = charSetArr[1].trim();
						break;
					}
				}
			}
			if (charSet == null)
				charSet = ui.getChartSet();
			else
				ui.setChartSet(charSet);
			// 当字符集为GB2312时转成父集GBK
			charSet = charSet.equalsIgnoreCase("GB2312") ? "GBK" : charSet;
			InputStream uis = null;
			try {
				uis = uc.getInputStream();
			} catch (IOException e) {
				if (ui.getPatternUrlId() == 100003
						&& (ui.getSortId() == 68 || ui.getSortId() == 69)) {
					ui.setHtmlContent("--");
					return;
				}

				if (UrlSupervise.isFirst != 0) {
					throw e;
				} else {
					ui.setHtmlContent("您访问的网页不存在");
					return;
				}
			}
			InputStreamReader reader;
			StringBuffer htmlStr = new StringBuffer();
			// 检测返回的页面源码是否被压缩
			try {
				if (uc.getHeaderField("Content-Encoding") != null
						&& uc.getHeaderField("Content-Encoding")
								.equalsIgnoreCase("gzip"))
					reader = new InputStreamReader(new GZIPInputStream(uis),
							charSet);
				else
					reader = new InputStreamReader(uis, charSet);
				int readNum = 0;
				while (true) {
					int b = reader.read();
					if (b == -1 || readNum > MAX_READER_LENGTH)
						break;
					htmlStr.append((char) b);
					readNum++;
				}
				reader.close();

			} catch (EOFException eex) {
				eex.printStackTrace();
			}
			ui.setHtmlContent(htmlStr.toString().replace("\r\n", "")
					.replace("\n", "").replace("\r", ""));
		}

	}

	// private static void trustAllHttpsCertificates() throws Exception {
	// javax.net.ssl.TrustManager[] trustAllCerts = new
	// javax.net.ssl.TrustManager[1];
	// javax.net.ssl.TrustManager tm = new miTM();
	// trustAllCerts[0] = tm;
	// javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
	// .getInstance("SSL");
	// sc.init(null, trustAllCerts, null);
	// javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
	// .getSocketFactory());
	// }

	static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
			return;
		}
	}

	static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}

	public static void main(String[] args) throws Exception {
		// getCook();
		// getUrl411();
		// System.out.println(url411.size());
		// for (String u : url411) {
		// System.out.println(u);
		// }
		//
		Url ui = new Url(
				"http://review.suning.com/ajax/review_lists/general-000000000108928705-0000000000-total-5-default-10-----reviewList.htm",
				"", 0);
		// Url ui = new Url(
		// "http://item.jd.com/1950352522.html",
		// "", 0);
		// ui.setPreantUrl("http://www.suning.com/webapp/wcs/stores/ItemPrice/000000000121570685_0070073371_9017__1.html");
		ui.setChartSet("GBK");
		ui.setPatternUrlId(100003);
		ui.setSortId(68);
		// String cookieStr = DnsManager.getCookieStr(ui.getUrl())
		// + "lgc=tuoxin126;";
		// System.out.println(cookieStr);
		// String str = GetMD5.GetMD5Code(UUID.randomUUID().toString())
		// .toLowerCase();
		// String num="";
		// for (int i = 0; i < 32; i++){
		// if(Math.random()*10>5){
		// num+=(char)((int)'a'+(int)(Math.random()*26));
		// }else{
		// num+=(int) (Math.random() * 10);
		// }
		// }
		for (int i = 0; i < 100; i++) {

			// System.out.println(num);
			// cookieMap.put("4", "t=" + num);
			long start = System.currentTimeMillis();
			try {
				if (ui.getUrl().indexOf("https") == 0) {
					System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!11");
					(new GetWebPage().new GetPage()).getPageInfoHttps(ui);
				} else {
					(new GetWebPage().new GetPage()).getPageInfo(ui);
				}
				// (new GetWebPage().new GetPage()).getPageInfoHttps(ui);
				String HtmlStr = ui.getHtmlContent();
				// String
				System.out.println(System.currentTimeMillis() - start
						+ "~~~~~~~~~~~~`");
				System.out.println(HtmlStr);
				System.out.println(ui.getChartSet());
				start = System.nanoTime();// .replace("	","").replace(" ", "")
				// String
				// patternStr="id=\"merchantID\" name=\"merchantID\" value=\"([^\"]*?)\" />";
				// String patternStr="由\\s*?<b>([^<]*?)</b>";
				// String patternStr="<a[^>]*?href=\"[^\"]*?\">全新品";
				// String
				// patternStr="<span id=\"priceblock_ourprice\" class=\"a-size-medium a-color-price\">\\s*?￥(\\S+?)\\s*?</span>";
				// String
				// patternStr="<span class=\"a-color-price price3P a-text-bold\">\\s*?￥(\\S+?)\\s*?</span>";
				String patternStr = "<li><cite>批准文号：</cite>\\s*?<select[^>]*?>\\s*?<option[^>]*?>[^>]*?</option>(.*?)</select>";
				// patternStr = "";
				Pattern testp = Pattern.compile(patternStr);
				Matcher m = testp.matcher(HtmlStr);
				int j = 0;
				while (m.find()) {
					String groupStr = m.group(0);
					System.out.println("aaaaaaaaa=" + groupStr);
					j++;
					System.out.println("****************************** start"
							+ j);
					String gP = "<a href=\"([^\"]*?)\"[^>]*?>\\s*?(\\S*?)</a>\\s*?<br\\s*?/>";
					gP = "<option\\s*?value=\"([^\"]*?)\"[^>]*?>([^>]*?)</option>";
					PatternMatcher pm = new PatternMatcher();
					List<String[]> re = pm.find(gP, groupStr);
					for (String[] a : re) {
						i++;
						ui.setUrl(a[1]);
						UrlSupervise.handleUrlString(ui);
						System.out.println(a[1]);
					}
					System.out
							.println("****************************** end-----------"
									+ j);
					System.out.println(Base64.encode(patternStr.getBytes())
							+ " length="
							+ Base64.encode(patternStr.getBytes()).length());
					System.out.println(Base64.encode(gP.getBytes())
							+ " length="
							+ Base64.encode(gP.getBytes()).length());
				}
				System.out
						.println((System.nanoTime() - start) / 1000000 + "毫秒"); // 结束微秒
				System.out.println("------------------" + i);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// System.exit(0);
			}
		}
		System.exit(0);
	}
}
