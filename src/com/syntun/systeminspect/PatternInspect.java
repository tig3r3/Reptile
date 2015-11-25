package com.syntun.systeminspect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.syntun.tools.ConnectSql;

/**
 * 正则数据正确性检测
 * 
 */
public class PatternInspect {
	// 所有表ID
	private ArrayList<Integer> tableIds;
	// 所有表字段名称
	private ArrayList<String> fields;

	/**
	 * 执行检测入口
	 */
	public static void doInspect() {
		PatternInspect pc = new PatternInspect();
		pc.getFields();
		pc.getTableIds();
		LinkedList<LinkedList<HashMap<String, String>>> tb = pc
				.getPatternTable();
		for (int i = 0; i < tb.size(); i++) {
			LinkedList<HashMap<String, String>> tbi = new LinkedList<HashMap<String, String>>();
			tbi = tb.get(i);
			for (int j = 0; j < tbi.size(); j++) {
				HashMap<String, String> tbRow = null;
				tbRow = tbi.get(j);
				// 开始验证
				pc.InspectIt(tbRow.get("pattern_url_id"),
						tbRow.get("pattern_table_name"));
			}
		}
	}

	/**
	 * 获取所有站点的url_id和正则表表名信息
	 * 
	 * @return
	 */
	private LinkedList<LinkedList<HashMap<String, String>>> getPatternTable() {
		Connection conn = ConnectSql.getConn();
		LinkedList<LinkedList<HashMap<String, String>>> gp = new LinkedList<LinkedList<HashMap<String, String>>>();
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT url_group,pattern_table FROM init_url_list GROUP BY url_group";
			ResultSet rs = stmt.executeQuery(sql);
			LinkedList<HashMap<String, String>> gpi = new LinkedList<HashMap<String, String>>();
			while (rs.next()) {
				HashMap<String, String> pl = new HashMap<String, String>();
				pl.put("url_group", rs.getString("url_group"));
				pl.put("pattern_table", rs.getString("pattern_table"));
				gpi.add(pl);
			}
			gp.add(gpi);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("读取wgdata表失败!");
			System.exit(0);
		}
		ConnectSql.push(conn);
		return gp;
	}

	/**
	 * 检测主函数
	 * 
	 * @param url_id
	 * @param table_name
	 */
	private void InspectIt(String url_id, String table_name) {
		// 所有正则ID
		ArrayList<Integer> patternIds = this.getPatternIds(url_id, table_name);
		// 所有拼接ID
		ArrayList<Integer> purlIds = this.getPurlIds(url_id);
		Connection conn = ConnectSql.getConn();
		try {
			Statement stmt = conn.createStatement();
			String sql = "select * from " + table_name + " where url_id="
					+ url_id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// System.out.println("pattern_id:"+rs.getString("pattern_id"));
				/** 开始验证 */
				if (rs.getInt("pate_type_id") == 0) {
					System.out
							.println("----error----正则表出错了：pate_type_id=0或null了！！！");
					System.exit(0);
				}
				if (rs.getString("group_index") == ""
						|| rs.getString("group_index") == null) {
					System.out
							.println("----error----正则表出错了：group_index为空或null了！！！");
					System.exit(0);
				}
				if (patternIds.contains(rs.getInt("parent_pattern_id")) == false
						&& rs.getInt("parent_pattern_id") != 0) {
					System.out
							.println("----error----正则表出错了：parent_pattern_id不为数字或不存在该pattern_id！！！");
					System.exit(0);
				}
				String[] aboutPatternIds = rs.getString("about_pattern_id")
						.split(",");
				for (int i = 0; i < aboutPatternIds.length; i++) {
					if (aboutPatternIds[i] != ""
							&& patternIds.contains(Integer
									.parseInt(aboutPatternIds[i])) == false
							&& Integer.parseInt(aboutPatternIds[i]) != 0) {
						System.out
								.println("----error----正则表出错了：about_pattern_id 只能为0和已存在的pattern_id，多个必须用逗号分割！！！");
						System.exit(0);
					}
				}
				String[] aboutPurlIds = rs.getString("purl_id").split(",");
				for (int i = 0; i < aboutPurlIds.length; i++) {
					if (aboutPurlIds[i] != ""
							&& purlIds.contains(Integer
									.parseInt(aboutPurlIds[i])) == false
							&& Integer.parseInt(aboutPurlIds[i]) != 0) {
						System.out
								.println("----error----正则表出错了：purl_id 只能为0和已存在的purl_id，多个必须用逗号分割！！！");
						System.exit(0);
					}
				}
				if (this.fields.contains(rs.getString("col_name")) == false) {
					System.out
							.println("----error----正则表出错了：col_name 必须存在于字段表中！！！");
					System.exit(0);
				}
				if (this.tableIds.contains(rs.getInt("table_id")) == false
						&& rs.getInt("table_id") != 0) {
					System.out
							.println("----error----正则表出错了：able_id 必须存在于table表中或为0！！！");
					System.exit(0);
				}
				if (rs.getInt("is_save_page_data") != 0
						&& rs.getInt("is_save_page_data") != 1) {
					System.out
							.println("----error----正则表出错了：is_save_page_data 只能为1、0！！！");
					System.exit(0);
				}
				if (rs.getInt("is_get_url") != 0
						&& rs.getInt("is_get_url") != 1) {
					System.out
							.println("----error----正则表出错了：is_get_url 只能为1、0！！！");
					System.exit(0);
				}
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
	}

	/**
	 * 返回所有的正则ID
	 * 
	 * @param url_id
	 * @param table_name
	 * @return
	 */
	private ArrayList<Integer> getPatternIds(String url_id, String table_name) {
		Connection conn = ConnectSql.getConn();
		Statement stmt;
		// 所有pattern ID
		ArrayList<Integer> patterIds = new ArrayList<Integer>();
		try {
			stmt = conn.createStatement();
			String sql = "select * from " + table_name + " where url_id="
					+ url_id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				patterIds.add(rs.getInt("pattern_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ConnectSql.push(conn);
		return patterIds;
	}

	/**
	 * 返回所有的拼接ID
	 * 
	 * @param url_id
	 * @return
	 */
	private ArrayList<Integer> getPurlIds(String url_id) {
		Connection conn = ConnectSql.getConn();
		Statement stmt;
		ArrayList<Integer> purlIds = new ArrayList<Integer>();
		try {
			stmt = conn.createStatement();
			String sql = "select id from purl_list where url_group=" + url_id;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				purlIds.add(rs.getInt("id"));
			}
			ConnectSql.push(conn);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return purlIds;

	}

	/**
	 * 返回所有字段名
	 */
	private void getFields() {
		Connection conn = ConnectSql.getConn();
		Statement stmt;
		ArrayList<String> fileds = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			String sql = "select filed_name from data_table_filed_list group by filed_name";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				fileds.add(rs.getString("filed_name"));
			}
			rs.close();
			stmt.close();
			ConnectSql.push(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.fields = fileds;
	}

	/**
	 * 返回所有表ID
	 */
	private void getTableIds() {
		Connection conn = ConnectSql.getConn();
		Statement stmt;
		ArrayList<Integer> tableIds = new ArrayList<Integer>();
		try {
			stmt = conn.createStatement();
			String sql = "select table_id from data_table_list";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				tableIds.add(rs.getInt("table_id"));
			}
			ConnectSql.push(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.tableIds = tableIds;
	}

	/**
	 * 测试主函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

	}
}
