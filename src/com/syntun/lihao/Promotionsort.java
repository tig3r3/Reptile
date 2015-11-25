package com.syntun.lihao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.syntun.entity.Promotion;
import com.syntun.tools.ConnectSql;

public class Promotionsort {

	/**
	 * 得到促销种类
	 * 
	 * @param database
	 *            数据库名
	 * @param datatime
	 *            get_date参数
	 * 
	 */
	public List<Promotion> getPsort(String database, String datetime) {
		Connection conn = ConnectSql.getConn();
		List<Promotion> prolist = new ArrayList<Promotion>();
		String sql = "SELECT DISTINCT promotion_type_name FROM `" + database
				+ "`.`promotion_info` WHERE get_date='" + datetime + "' ";
		System.out.println(sql);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println("----------------------------------------------------");
			System.out.println("当前数据库：" + database + "--get_data:" + datetime);
			while (rs.next())
				prolist.add(new Promotion(database, rs.getString(1), datetime));
			// System.out.println(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectSql.push(conn);
		}
		return prolist;
	}

	public boolean insertData(Promotion pro) {
		boolean bool = false;
		Connection conn = DBConn.getConn();
		String sql = "INSERT IGNORE  INTO promotion_type VALUES(DEFAULT,'"
				+ pro.getDatabase() + "','" + pro.getProname() + "','" + pro.getDate() + "')";
		try {
			Statement stmt = conn.createStatement();
			bool = stmt.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectSql.push(conn);
		}
		return bool;
	}

	public static void main(String[] args) {
		Promotionsort pro = new Promotionsort();
		List<String> list = DBConn.getDatabase();
		List<Promotion> prolist = null;
		HashMap<String, List<Promotion>> maps = new HashMap<String, List<Promotion>>();
		for (int i = 0; i < list.size(); i++) {
			maps.put(list.get(i), pro.getPsort(list.get(i), "2014-05-02"));
		}
		Iterator<String> it = maps.keySet().iterator();
		while (it.hasNext()) {
			prolist = maps.get(it.next());
			for (int i = 0; i < prolist.size(); i++) {
				if (pro.insertData(prolist.get(i))) {
					// System.out.println("yyyyyyyyyyyyyy");
				}
			}
		}

		System.out.println("exit");
	}
}
