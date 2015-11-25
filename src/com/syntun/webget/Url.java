package com.syntun.webget;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import com.syntun.tools.ConnectSql;
import com.syntun.wetget.annotation.SetProperty;

/**
 * 
 * 保存当前页需要带入下一级页面的信息
 * 
 */
@SetProperty(propertyVars = { "isSaveCookie" }, propertyComment = "是否保存cookie")
public class Url {

	private boolean status;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public static int cObjNum = 0;
	/**
	 * 当前地址
	 */
	private String url;
	/**
	 * 父目录地址
	 */
	private String preantUrl;
	
	private int sortId;
	
	/**
	 * 表中自增
	 */
	private int urlId;

	private int hostId;

	private String host;

	private int patternId = 0;

	private int pUrlId = 0;

	private int getUrlId = 0;

	private int patternUrlId = 0;

	private int fatherUrlId = 0;
	/**
	 * 存储该页数据，子页可以调用
	 */
	private HashMap<String, String> savePageInfo = new HashMap<String, String>();

	private String htmlContent = null;

	private String replaceUrlAnd = "%-%";

	private String replaceUrldengyu = "#-#";

	private String chartSet = "UTF-8";

	private HashMap<String, String> saveCookieHm = new HashMap<String, String>();
	/**
	 * 记录已解析完成的地址ID
	 */
	private static LinkedList<Integer> parseGetUrlIdList = new LinkedList<Integer>();
	/**
	 * 自动更新已抓取完成URL的状态的间隔时间
	 */
	private final static int SLEEP_UPDATA_TIME = 20;
	/**
	 * 是否保存cookie
	 */
	private static boolean isSaveCookie = true;
	
	
	public int getSortId() {
		return sortId;
	}

	public void setSortId(int sortId) {
		this.sortId = sortId;
	}

	/**
	 * 解析完成的地址ID
	 * 
	 * @param getUrlId
	 */
	private static void addParseOverGetUrlId(int getUrlId) {
		synchronized (parseGetUrlIdList) {
			parseGetUrlIdList.add(getUrlId);
		}
	}
	
	public Url(){}
	
	/**
	 * 批量更新线程
	 * 
	 */
	static class UpdateParseOverStatus implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					Thread.sleep(Url.SLEEP_UPDATA_TIME * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 拼接sql
				String sql = "update url_status set parse_time=now(),is_parse_over=1,try_num=0 where id in (";
				int i = 0;
				synchronized (parseGetUrlIdList) {
					int urlIdSize = parseGetUrlIdList.size();
					for (i = 0; i < urlIdSize; i++) {
						if (i != 0)
							sql += ",";
						sql += parseGetUrlIdList.get(i);
					}
					parseGetUrlIdList.clear();
				}
				sql += ")";
				// 执行更新
				if (i > 0) {
					Connection conn = ConnectSql.getConn();
					try {
						Statement stmt = conn.createStatement();
						stmt.executeUpdate(sql);
						stmt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ConnectSql.push(conn);
				}
			}
		}
	}

	/**
	 * 启动更新线程静态代码块
	 */
	static {
		new Thread(new UpdateParseOverStatus()).start();
	}

	public Url(String url, int patternId) {
		this.url = url;
		Url.addObjNum(1);
		this.patternId = patternId;
	}

	public Url(String url, String preantUrl, int sortId) {
		this.url = url;
		this.preantUrl = preantUrl;
		if (this.url.indexOf("&amp;") != -1) {
			this.url = this.url.replaceAll("&amp;", "&");
		}
		this.sortId = sortId;
		Url.addObjNum(1);
	}

	public Url(String url, Url ui) {
		this.url = url;
		this.host = ui.getHost();
		this.hostId = ui.getHostId();
		this.preantUrl = ui.getUrl();
		this.patternUrlId = ui.getPatternUrlId();
		this.chartSet = ui.getChartSet();
		this.fatherUrlId = ui.getUrlId;

		this.setCookie(ui.getCookie());
		HashMap<String, String> data = ui.getData();
		this.savePageInfo = new HashMap<String, String>();
		for (Iterator<Entry<String, String>> iter = data.entrySet().iterator(); iter
				.hasNext();) {
			Entry<String, String> e = iter.next();
			this.savePageInfo.put(e.getKey(), e.getValue());
		}
		ui = null;
	}

	public Url(String url, Url ui, int sortId) {
		this.url = url;
		this.host = ui.getHost();
		this.hostId = ui.getHostId();
		this.preantUrl = ui.getUrl();
		this.patternUrlId = ui.getPatternUrlId();
		this.chartSet = ui.getChartSet();
		this.sortId = sortId;
		this.setCookie(ui.getCookie());
		this.fatherUrlId = ui.getGetUrlId();
		HashMap<String, String> data = ui.getData();
		this.savePageInfo = new HashMap<String, String>();
		for (Iterator<Entry<String, String>> iter = data.entrySet().iterator(); iter
				.hasNext();) {
			Entry<String, String> e = iter.next();
			this.savePageInfo.put(e.getKey(), e.getValue());
		}
		ui = null;
	}

	public void finalize() {
		Url.addObjNum(-1);
	}

	/**
	 * 设置组合URL ID
	 * 
	 * @param pUrlId
	 */
	public void setPUrlId(int pUrlId) {
		this.pUrlId = pUrlId;
	}

	/**
	 * 添加数据
	 * 
	 * @param key
	 * @param value
	 */
	public void addData(String key, String value) {
		this.savePageInfo.put(key, value);
	}

	public void addData(HashMap<String, String> data) {
		// this.savePageInfo = new HashMap<String,String>();
		for (Iterator<Entry<String, String>> iter = data.entrySet().iterator(); iter
				.hasNext();) {
			Entry<String, String> e = iter.next();
			this.savePageInfo.put(e.getKey(), e.getValue());
		}
	}

	public void addData(String dataStr) {
		try {
			if (dataStr != null && !dataStr.equals("")) {
				String[] dataArr = dataStr.split("&");
				for (int i = 0; i < dataArr.length; i++) {
					String[] dataInfo = dataArr[i].split("=");
					try {
						this.savePageInfo.put(dataInfo[0], dataInfo[1]
								.replaceAll(this.replaceUrlAnd, "&")
								.replaceAll(this.replaceUrldengyu, "="));
					} catch (Exception e) {
//						System.out.println("err data = " + dataArr[i]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setHostId(int hostId) {
		this.hostId = hostId;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setGetUrlId(int getUrlId) {
		this.getUrlId = getUrlId;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public HashMap<String, String> getData() {
		return this.savePageInfo;
	}

	public String getData(String key) {
		if (this.savePageInfo.containsKey(key))
			return this.savePageInfo.get(key);
		else
			return null;
	}

	public String getDataStr() {
		String urlDataStr = "";
		if (!this.getData().isEmpty()) {
			int i = 0;
			for (Iterator<Entry<String, String>> iter = this.getData()
					.entrySet().iterator(); iter.hasNext(); i++) {
				Entry<String, String> e = iter.next();
				if (i != 0)
					urlDataStr += "&";
				urlDataStr += e.getKey()
						+ "="
						+ e.getValue().replaceAll("&", this.replaceUrlAnd)
								.replaceAll("=", this.replaceUrldengyu);
			}
		}
		return urlDataStr;
	}

	public String getUrl() {
		return this.url;
	}

	public String getPreantUrl() {
		return this.preantUrl;
	}

	public void setPreantUrl(String url) {
		this.preantUrl = url;
	}

	public String getHtmlContent() {
		return this.htmlContent;
	}

	public int getUrlId() {
		return this.urlId;
	}

	public String getHost() {
		return this.host;
	}

	public int getPatternId() {
		return this.patternId;
	}

	public int getHostId() {
		return this.hostId;
	}

	public synchronized static void addObjNum(int num) {
		Url.cObjNum += num;
	}

	public int getPUrlId() {
		return this.pUrlId;
	}

	public int getGetUrlId() {
		return this.getUrlId;
	}

	public void setPatternUrlId(int patternUrlId) {
		this.patternUrlId = patternUrlId;
	}

	public int getPatternUrlId() {
		return this.patternUrlId;
	}

	public void setParseOver() {
		if (this.getUrlId == 0)
			return;
		Url.addParseOverGetUrlId(getUrlId);
	}

	public void setCookie(String cookieStr) {
		if (isSaveCookie) {
			if (cookieStr == null || cookieStr.equals(""))
				return;
			String[] cookieArr = cookieStr.split("; ");
			for (int i = 0; i < cookieArr.length; i++) {
				String[] kv = cookieArr[i].split("=");
				if (kv.length == 1)
					this.saveCookieHm.put(kv[0].trim(), "");
				else if (kv.length == 2)
					this.saveCookieHm.put(kv[0].trim(), kv[1].trim());
			}
		}
	}

	public String getCookie() {
		String cookieStr = "";
		int i = 0;
		for (Iterator<Entry<String, String>> iter = this.saveCookieHm
				.entrySet().iterator(); iter.hasNext(); i++) {
			Entry<String, String> e = iter.next();
			if (i != 0)
				cookieStr += "; ";
			cookieStr += e.getKey() + "=" + e.getValue();
			i++;
		}
		return i == 0 ? null : cookieStr;
	}

	public void setChartSet(String chartSet) {
		if (chartSet != null && !chartSet.equals(""))
			this.chartSet = chartSet;
	}

	public String getChartSet() {
		return this.chartSet;
	}

	public int getFatherUrlId() {
		return fatherUrlId;
	}

	public void setFatherUrlId(int fatherUrlId) {
		this.fatherUrlId = fatherUrlId;
	}

}
