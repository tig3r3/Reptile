package com.syntun.tools;

import java.sql.*;
import java.util.*;

import com.syntun.webget.ResolveWebPage;

public class TMConnectSql {
	private static final int CONN_TIME_OUT = 288000;
	public static String sqlName = "wgdata";
	public static String sqlPassWord = "syntun-000";
	public static String sqlDbName = "wgdata";
	private static String sqlConStr = null;
	private static LinkedList<Connection> liConn = new LinkedList<Connection>();
	private static LinkedList<String> sqlCache = new LinkedList<String>();
	private static LinkedList<Statement> bacthStmt = new LinkedList<Statement>();
	private static int connCreterNum = 0;
	private static int maxConnNum = 10;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	private synchronized static void connCreaterNumChange(int num) {
		System.err.println("+++++++++connCreterNum=" + connCreterNum
				+ " liConnLength = " + liConn.size());
		connCreterNum += num;
		if (connCreterNum < 0)
			connCreterNum = 0;
	}

	public static int getMaxNum(String productId, String tableName) {
		int maxNum = 0;
		Connection con = TMConnectSql.getConn();
		try {
			PreparedStatement ps = con
					.prepareStatement("SELECT buy_total_num FROM " + tableName
							+ " WHERE product_id='" + productId
							+ "' ORDER BY get_date DESC LIMIT 0,1;");
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				maxNum = rs.getInt("buy_total_num");
			}
			rs.close();
			TMConnectSql.push(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return maxNum;
	}

	/**
	 * 获取数据库链接
	 * 
	 * @return
	 * 
	 */
	public static Connection getConn() {
		if (ResolveWebPage.sqlHost == null || ResolveWebPage.sqlHost.equals("")) {
			ResolveWebPage.sqlHost = "192.168.0.69";
		}
		synchronized (liConn) {
			if (sqlConStr == null) {
				sqlConStr = "jdbc:mysql://"
						+ ResolveWebPage.sqlHost
						+ "/"
						+ sqlDbName
						+ "?user="
						+ sqlName
						+ "&password="
						+ sqlPassWord
						+ "&seUnicode=true&characterEncoding=UTF8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10";
			}
			Connection conn = null;
			try {
				if (liConn.isEmpty() || liConn.size() == 0) {

					if (connCreterNum < maxConnNum) {
						liConn.add(DriverManager.getConnection(sqlConStr));
						connCreaterNumChange(1);
					} else {
						while (liConn.size() == 0 || connCreterNum < maxConnNum) {
							Thread.sleep(300);
						}

					}
				}
				conn = (Connection) liConn.pollFirst();
				// 判断链接是否有效
				if (conn.isClosed() || !conn.isValid(CONN_TIME_OUT)) {
					connCreterNum--;
					return getConn();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("connection db error!!!");
				System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return conn;
		}
	}

	public static void push(Connection conn) {
		synchronized (liConn) {
			if (connCreterNum < maxConnNum) {
				try {
					if (!conn.isClosed()) {
						if (!conn.getAutoCommit()) {
							conn.commit();
							conn.setAutoCommit(true);
						}
						conn.clearWarnings();
					} else
						conn = DriverManager.getConnection(sqlConStr);

					liConn.add(conn);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else {
				try {
					conn.close();
					connCreaterNumChange(-1);
				} catch (Exception e) {
					conn = null;
				}
			}
		}
	}

	public static synchronized void addStmt(Statement stmt) {
		int s = TMConnectSql.bacthStmt.indexOf(stmt);
		if (s == -1)
			TMConnectSql.bacthStmt.add(stmt);
	}

	public static synchronized void removeStmt(Statement stmt) {
		int s = TMConnectSql.bacthStmt.indexOf(stmt);
		if (s != -1)
			TMConnectSql.bacthStmt.remove(s);
	}

	public static synchronized int exeAllBacth() throws SQLException {
		int sNum = 0;
		while (TMConnectSql.bacthStmt.size() > 0) {
			sNum += TMConnectSql.bacthStmt.remove(0).executeBatch().length;
		}
		return sNum;
	}

	public static synchronized void cacheInserSql(String sql, Statement stmt) {
		if (TMConnectSql.sqlCache.size() >= 50) {
			try {
				for (int i = 0; i < TMConnectSql.sqlCache.size(); i++)
					stmt.addBatch(TMConnectSql.sqlCache.get(i));

				stmt.executeBatch();
				stmt.clearBatch();
				stmt.clearWarnings();
			} catch (Exception e) {
				System.out.println("执行缓存sql错误");
				System.exit(0);
			}
			TMConnectSql.sqlCache.clear();
			TMConnectSql.sqlCache = new LinkedList<String>();
		}
		TMConnectSql.sqlCache.add(sql);

	}
}
