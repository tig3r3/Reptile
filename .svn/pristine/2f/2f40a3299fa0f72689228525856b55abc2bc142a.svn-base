package com.syntun.cookie;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.syntun.tools.ConnectSql;
import com.syntun.webget.GetWebPage;

public class GetTmallCookie implements Runnable {
	static HashMap<String, String> cookieUserMap = new HashMap<String, String>();

	static List<String> list = new ArrayList<String>();
	static int isCookie = 0;

	static int cookieNum = 0;

	static {
		Connection conn = (Connection) ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			// 加载地址过滤正则
			String sql = "SELECT `user_name`, `user_pwd`, `url`, `url_group` FROM `wgdata`.`cookie_user`";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				cookieUserMap
						.put(rs.getString("user_name") + ","
								+ rs.getString("user_pwd"), rs.getString("url"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.exit(0);
		}
		ConnectSql.push(conn);
	}

	public static void addCookie() {
		List<String> li = new ArrayList<String>();
		for (String key : cookieUserMap.keySet()) {
			try {
				Map<String, String> params = new HashMap<String, String>();
				// 登陆天猫
				params.put("TPL_username", key.split(",")[0]);
				params.put("TPL_password", key.split(",")[1]);
				String url = cookieUserMap.get(key);
				String charset = "utf-8";
				// 验证账号并获取cookie
				Result result = SendRequest
						.sendPost(url, null, params, charset);
				if (result.getCookie().indexOf(key.split(",")[0]) != -1) {
					li.add(result.getCookie());
				}else{
					System.out.println(result.getCookie());
				}
			} catch (UnknownHostException e) {
				System.out.println("没网了");
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if ((li.size() == list.size()) || isCookie == 0) {
			isCookie = 1;
			list.clear();
			list.addAll(li);
		} else {
			System.out.println("没有更新cookie成功一次");
		}
	}

	public static String getCookie() {
		String cookie = null;
		if (cookieNum == list.size()) {
			cookieNum = 0;
		}
		if (list.size() != 0) {
			cookie = list.get(cookieNum);
			cookieNum++;
		}
		System.out.println(list.size());
		return cookie;
	}

	public static void main(String[] args) throws Exception {
		addCookie();
		System.out.println(getCookie());
		System.out.println(getCookie());
//		addCookie();
		System.out.println(getCookie());
		System.out.println(getCookie());
	}

	@Override
	public void run() {
		while (true) {
			addCookie();
			try {
				Thread.sleep(1000 * 60 * GetWebPage.cookieTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
