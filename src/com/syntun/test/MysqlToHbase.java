package com.syntun.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import com.syntun.tools.GetMD5;
import com.syntun.webget.Url;

public class MysqlToHbase {
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	static String sqlConStr = "jdbc:mysql://192.168.0.70/wgdata?user=wgdata&password=syntun-000&seUnicode=true&characterEncoding=UTF8";

	static HashMap<String, String> urlHosts = new HashMap<String, String>();

	public static void main(String[] args) {
		try {
			getUrlHosts();
			for (String key : urlHosts.keySet()) {
				new Thread(new runAdd(key, urlHosts.get(key))).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static class runAdd implements Runnable {

		String key;
		String value;

		public runAdd(String key, String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public void run() {
			try {

				boolean status = false;
				while (true) {
					Connection con = DriverManager.getConnection(sqlConStr);
					if (status) {
						status = false;
						Thread.sleep(1000 * 60 * 5);
					}
					String sql = "SELECT id,`url_group`,`url_str`,`father_url`,`url_data`,`sort_id`,`url_charset` FROM `url_list` WHERE id IN( SELECT `id` FROM `url_status` WHERE is_parse_over=2) and url_group in("
							+ value + ") limit 0,3000";
					PreparedStatement ps = con.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					LinkedList<Url> list = new LinkedList<Url>();
					StringBuffer sb = new StringBuffer(
							"UPDATE `url_status` SET `is_parse_over` = 4 WHERE `id` IN (0");
					while (rs.next()) {
						Url u = new Url();
						u.setPreantUrl(rs.getString("father_url"));
						u.setUrl(rs.getString("url_str"));
						u.addData(rs.getString("url_data"));
						u.setPatternUrlId(rs.getInt("url_group"));
						u.setChartSet(rs.getString("url_charset"));
						u.setSortId(rs.getInt("sort_id"));
						list.add(u);
						sb.append("," + rs.getString("id"));
					}
					sb.append(")");
					PreparedStatement ps1 = con.prepareStatement(sb.toString());
					if (list.size() == 0) {
						status = true;
					}
					System.out.println(System.currentTimeMillis());
					execInsertUrl(list, key);
					System.out.println(System.currentTimeMillis());
					ps1.execute();

					ps.close();
					ps1.close();
					con.close();
					System.out.println(list.size() + ":" + key);
					System.gc();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void getUrlHosts() throws Exception {
		Connection con = DriverManager.getConnection(sqlConStr);
		String sql = "SELECT `hosts`, `urlGroup` FROM urlGroup_hosts";
		PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			urlHosts.put(rs.getString("hosts"), rs.getString("urlGroup"));
		}
		con.close();
	}

	/**
	 * 插入到数据库
	 */
	public synchronized static void execInsertUrl(LinkedList<Url> li, String key) {
		String uuid = UUID.randomUUID().toString();
		String sqlConStr = "jdbc:mysql://"
				+ key
				+ "/wgdata?user=wgdata&password=syntun-000&seUnicode=true&characterEncoding=UTF8";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(sqlConStr);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		GetMD5 gmd5 = new GetMD5();
		try {
			// 插入内存表sql
			String insertMsql = "insert ignore into url_status(add_time,url_md5,insert_batch_num,is_parse_over) values(NOW(),?,'"
					+ uuid + "',?)";
			// 插入硬盘表sql
			String insertDsql = "insert ignore into url_list(url_md5,url_str,father_url,url_group,url_data,id,url_charset,sort_id) "
					+ "values(?,?,?,?,?,?,?,?)";
			// 插入内存表
			PreparedStatement pstmt = conn.prepareStatement(insertMsql);
			// String statusSql =
			// "insert ignore into url_status(add_time,url_md5,insert_batch_num,is_parse_over) values";
			// StringBuffer sbStatus = new StringBuffer(statusSql);
			int i = 0;
			for (; i < li.size(); i++) {
				Url urlInfoObj = li.get(i);
				String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
						+ urlInfoObj.getPatternUrlId());
				// if (i != 0) {
				// sbStatus.append(",");
				// }
				// sbStatus.append("('NOW()','").append(md5Code).append("','")
				// .append(uuid).append("',0)");
				pstmt.setString(1, md5Code);
				pstmt.setInt(2, 0);
				pstmt.addBatch();
				if (i % 100 == 0) {
					pstmt.executeBatch();
				}
			}
			if (i % 100 != 0) {
				pstmt.executeBatch();
				pstmt.clearBatch();
			}
			// // 插入内存表
			// if (sbStatus.toString().equals(statusSql)) {
			// return;
			// }
			// System.out.println(sbStatus.toString().equals(statusSql) + "___"
			// + sbStatus);
			pstmt = conn.prepareStatement(insertDsql);
			// boolean statusS = pstmt.execute();
			// if (statusS == false) {
			// System.out.println("~~~~~~~~~~~~~~插入内存表错误");
			// }
			// 查询出成功插入的数据
			HashMap<String, Integer> insertMd5Hm = new HashMap<String, Integer>();
			ResultSet rs = pstmt
					.executeQuery("select id,url_md5 from url_status where insert_batch_num='"
							+ uuid + "'");
			while (rs.next()) {
				insertMd5Hm.put(rs.getString("url_md5"), rs.getInt("id"));
			}
			pstmt.close();
			int batchNum = 0;
			// String listSql =
			// "insert ignore into url_list(url_md5,url_str,father_url,url_group,url_data,id,url_charset,sort_id) values";
			// StringBuffer strList = new StringBuffer(listSql);
			// int s = 0;
			pstmt = conn.prepareStatement(insertDsql);
			i = 0;
			for (; i < li.size(); i++) {
				Url urlInfoObj = li.get(i);
				String md5Code = gmd5.mD5Code(urlInfoObj.getUrl() + ":"
						+ urlInfoObj.getPatternUrlId());
				if (insertMd5Hm.containsKey(md5Code)) {
					// if (s != 0) {
					// strList.append(",");
					// }
					// s++;
					// strList.append("('").append(md5Code).append("','")
					// .append(urlInfoObj.getUrl()).append("','")
					// .append(urlInfoObj.getPreantUrl()).append("','")
					// .append(urlInfoObj.getPatternUrlId()).append("','")
					// .append(urlInfoObj.getDataStr()).append("','")
					// .append(insertMd5Hm.get(md5Code)).append("','")
					// .append(urlInfoObj.getChartSet()).append("','")
					// .append(urlInfoObj.getSortId()).append("')");
					// ;
					int insertId = insertMd5Hm.get(md5Code);
					pstmt.setString(1, md5Code);
					pstmt.setString(2, urlInfoObj.getUrl());
					pstmt.setString(3, urlInfoObj.getPreantUrl());
					pstmt.setInt(4, urlInfoObj.getPatternUrlId());
					pstmt.setString(5, urlInfoObj.getDataStr());
					pstmt.setInt(6, insertId);
					pstmt.setString(7, urlInfoObj.getChartSet());
					pstmt.setInt(8, urlInfoObj.getSortId());
					pstmt.addBatch();
					batchNum++;
					if (batchNum % 100 == 0) {
						pstmt.executeBatch();
						pstmt.clearBatch();
						pstmt.clearParameters();
						batchNum = 0;
					}
				}
			}
			if (batchNum != 0) {
				pstmt.executeBatch();
			}
			// if (strList.toString().equals(listSql)) {
			// return;
			// }
			// System.out.println(strList);
			// 插入硬盘表
			// pstmt = conn.prepareStatement(strList.toString());
			// boolean listStatus = pstmt.execute();
			// if (listStatus == false) {
			// System.out.println("~~~~~~~~~~~~~~insert url_list error");
			// }
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("插入地址错误");
			System.exit(0);
		}
	}
}
