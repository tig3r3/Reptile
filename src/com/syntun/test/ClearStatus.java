package com.syntun.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.syntun.tools.ConnectSql;
import com.syntun.webget.Start;

public class ClearStatus {
	public static void main(String[] args) {
		Start.initSystem();
		// 清空状态表
		String trunceStatusSql = "TRUNCATE TABLE `url_status`";
		// 清空地址列表
		String trunceListSql = "TRUNCATE TABLE `url_list`";
		Connection conn = ConnectSql.getConn();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			// 清空
			stmt.executeUpdate(trunceListSql);
			stmt.executeUpdate(trunceStatusSql);
			stmt.close();
			ConnectSql.push(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("~~~~~~~~~~~" + ConnectSql.sqlHost);
		System.exit(0);
	}
}
