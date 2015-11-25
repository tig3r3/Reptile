package com.syntun.lihao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CreateDataSum {
	public List<String> getData(String baseFrom, int id) {
		List<String> list = new ArrayList<String>();
		Connection conn = DBConn.getConn();
		// baseFrom.replaceAll("\\`", "");
		String a[] = baseFrom.split("\\.");

		String sql = "SHOW TABLE STATUS FROM " + a[0]
				+ " WHERE ENGINE IS NOT NULL AND NAME='" + a[1] + "'";
		System.out.println(sql);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				// CreateDataTime cdt=new CreateDataTime();
				sql = "INSERT INTO create_data_sum_day VALUES(DEFAULT,'"
						+ baseFrom + "'," + rs.getString(5) + ",DEFAULT," + id
						+ ",DEFAULT)";
				list.add(sql);
				// System.out.println("0000");
				// System.out.println(rs.getString(5));
			}
		} catch (SQLException e) {
			System.out.println(baseFrom + "不存在！！！！");
			// e.printStackTrace();
		} finally {
			DBConn.closeConn(conn);
		}
		return list;
	}

	public void insetData(String sql) {
		Connection conn = DBConn.getConn();
		// baseFrom.replaceAll("\\`", "");
		System.out.println(sql);
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBConn.closeConn(conn);
		}

	}

	// mian
	public void aa() {
		CreateDataSum s = new CreateDataSum();
		Map<Integer, String> map = DBConn.getFrom();
		List<List<String>> listsql = new ArrayList<List<String>>();
		Iterator<Integer> it = map.keySet().iterator();
		while (it.hasNext()) {
			int i = it.next();
			listsql.add(s.getData(map.get(i), i));
		}
		for (int i = 0; i < map.size(); i++) {

		}
		for (int i = 0; i < listsql.size(); i++) {
			for (int j = 0; j < listsql.get(i).size(); j++) {
				s.insetData(listsql.get(i).get(j));
			}
		}

	}

	public static void main(String[] args) {
		CreateDataSum s = new CreateDataSum();
		s.aa();
	}
}
