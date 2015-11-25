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
 * url地址的访问频率控制类
 * 
 * @author tuo
 * 
 */
public class UrlLimitThread implements Runnable {
	/**
	 * url地址列表
	 */
	private static LinkedList<Url> userUrlList = new LinkedList<Url>();

	public void adduserUrlList(Url ui) {
		synchronized (userUrlList) {
			userUrlList.add(ui);
		}
	}

	private UrlLimit urlLimit = null;

	public UrlLimitThread(UrlLimit urlLimit) {
		this.urlLimit = urlLimit;
	}

	@Override
	public void run() {
		while (true) {
			if (userUrlList.size() <= 10) {
				selectUrlInfo();
			}
			if (urlLimit.getInsertStatus()
					&& System.currentTimeMillis()
							- urlLimit.getUserInsertTime() >= urlLimit
								.getIntervalTime() && userUrlList.size() > 0) {
				synchronized (userUrlList) {
					UrlSupervise.addParseUserUrlInfo(userUrlList.pollFirst());
				}
				urlLimit.updateStatusToFalse();
			}
			SleepUtil.sleep(300);
		}
	}

	/**
	 * 查询待抓取地址
	 */
	private synchronized void selectUrlInfo() {
		String sql = "SELECT b.* FROM (SELECT id FROM `url_status` WHERE is_parse_over="
				+ urlLimit.getStatus()
				+ " and try_num<"
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
