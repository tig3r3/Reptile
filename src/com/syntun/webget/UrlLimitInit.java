package com.syntun.webget;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.syntun.tools.ConnectSql;

public class UrlLimitInit {
	public static List<UrlLimit> urlLimitList = new ArrayList<UrlLimit>();

	/**
	 * 初始化访问限制集合
	 */
	public static void init() {
		String sql = "SELECT `url_group`, `sort_id`, `status`, `interval_time`, `is_dns` FROM  `url_limit_list`";
		Connection conn = ConnectSql.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				UrlLimit urlLimit = new UrlLimit();
				urlLimit.setIntervalTime(rs.getInt("interval_time"));
				urlLimit.setStatus(rs.getInt("status"));
				urlLimit.setUrlGroup(rs.getString("url_group"));
				List<String> list = new ArrayList<String>();
				String[] sortStrs = rs.getString("sort_id").split(",");
				for (String str : sortStrs) {
					list.add(str);
				}
				urlLimit.setSortIdList(list);
				urlLimitList.add(urlLimit);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
		if (urlLimitList.size() > 0) {
			for (UrlLimit ul : urlLimitList) {
				UrlLimitThread ult = new UrlLimitThread(ul);
				new Thread(ult).start();
				ul.setUrlLimitThread(ult);
			}
		}
	}

	public static void main(String[] args) {
		init();
	}
}
