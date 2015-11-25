package com.syntun.webget;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;

import com.syntun.tools.ConnectSql;
import com.syntun.tools.SleepUtil;

/**
 * 
 * 当当url地址的访问频率控制类
 * 
 * @author tuo
 * 
 */
public class UserDangdang implements Runnable {
	/**
	 * url地址列表
	 */
	private static LinkedList<Url> userUrlList = new LinkedList<Url>();
	/**
	 * url地址被插入到
	 */
	public static Long userInsertTime = System.currentTimeMillis();
	/**
	 * 当前是否可以进行将当当的url地址添加到
	 */
	public static Boolean status = true;

	public static void adduserUrlList(Url ui) {
		synchronized (userUrlList) {
			userUrlList.add(ui);
		}
	}

	@Override
	public void run() {
		while (true) {
			if (userUrlList.size() <= 10) {
				selectUrlInfo();
			}
			if (status && System.currentTimeMillis() - userInsertTime >= 5000
					&& userUrlList.size() > 0) {
				synchronized (userUrlList) {
					UrlSupervise.addParseUserUrlInfo(userUrlList.pollFirst());
				}
				updateStatusToFalse();
			}
			SleepUtil.sleep(300);
		}
	}

	/**
	 * 更新状态和插入时间
	 */
	public synchronized static void updateStatusToTrue() {
		userInsertTime = System.currentTimeMillis();
		status = true;
	}

	/**
	 * 更新状态和插入时间
	 */
	public synchronized static void updateStatusToFalse() {
		userInsertTime = System.currentTimeMillis();
		status = false;
	}

	/**
	 * 查询待抓取地址
	 */
	private synchronized static void selectUrlInfo() {
		String sql = "SELECT b.* FROM (SELECT id FROM `url_status` WHERE is_parse_over=5 and try_num<"
				+ UrlSupervise.maxTryGetNum
				+ " and get_time<date_sub(now(), interval "
				+ UrlSupervise.tryGetUrlTime
				+ " minute) LIMIT 0, 100) a JOIN `url_list` b ON(a.id=b.id)";
		Connection conn = ConnectSql.getConn();
		try {
			Statement tmt = conn.createStatement();
			ResultSet rs = tmt.executeQuery(sql);
			String ids = "0";
			while (rs.next()) {
				String host = new URL(rs.getString("url_str")).getHost()
						.toLowerCase();
				Url ui = new Url(rs.getString("url_str"),
						rs.getString("father_url"), rs.getInt("sort_id"));
				ui.setHost(host);
				ui.addData(rs.getString("url_data"));
				ui.setGetUrlId(rs.getInt("id"));
				ui.setUrlId(rs.getInt("id"));
				ui.setPatternUrlId(rs.getInt("url_group"));
				ui.setChartSet(rs.getString("url_charset"));
				ui.setPreantUrl(rs.getString("father_url"));
				ids += "," + rs.getInt("id");
				userUrlList.add(ui);
			}
			rs.close();
			sql = "update url_status set get_time=now(),try_num=try_num+1 where id in ("
					+ ids + ")";
			tmt.executeUpdate(sql);
			tmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取地址错误！程序终止");
			System.exit(0);
		}
		ConnectSql.push(conn);
	}
}
